package sim.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import sim.app.ReportWriter;
import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class CoffeeShop implements Subject, Observer {
    // Queue of orders populated by producer for staff to serve
    // Staff then populate the kitchen queue
    private SharedQueue customers = new SharedQueue(QueueType.CUSTOMER);
    private SharedQueue priorityCustomers = new SharedQueue(QueueType.PRIORITY);
    private SharedQueue orders = new SharedQueue(QueueType.KITCHEN);

    // List of registered observers for observer/subject pattern
    private LinkedList<Observer> observers = new LinkedList<>();

    // List of staff are observable
    private LinkedList<Server> servers = new LinkedList<>();

    // Kitchen is observable
    private Kitchen kitchen = new Kitchen(orders);

    public static final int NUM_STAFF = 3;
    private int numStaff;

    public CoffeeShop(Menu menu) {
        // Producer inserts input file of orders into shared queue for staff
        Thread producer = new Thread(new Producer(
            new File("data/orders.csv"),
            customers,
            menu
        ));
        Thread priorityProducer = new Thread(new Producer(
            new File("data/orders.csv"),
            priorityCustomers,
            menu
        ));

        producer.start();
        priorityProducer.start();
        
        numStaff = NUM_STAFF;
        updateServer();

        // Observe kitchen to later check when service has stopped
        kitchen.registerObserver(this);

        Thread kitchenT = new Thread(kitchen);
        kitchenT.start();
    }

    public void updateServer() {
        // Staff members consumes the queue of customer orders
        for (int i = 0; i < numStaff; i++) {
            // Track the staff members
            Server staff = new Server(customers, orders, priorityCustomers);
            servers.add(staff);

            // Observe staff to later check when service has stopped
            staff.registerObserver(this);

            // Start service
            Thread staffT = new Thread(staff);
            staffT.start();
        }
    }

    public SharedQueue getCustomers() {
        return customers;
    }

    public SharedQueue getPriorityCustomers() {
        return priorityCustomers;
    }

    public SharedQueue getOrders() {
        return orders;
    }

    public LinkedList<Server> getServers() {
        return servers;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public int getNumStaff() {
        return numStaff;
    }

    public void setNumStaff(int num) {
        this.numStaff = num;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) o.update();
    }

    @Override
    public void update() {
        // When all servers are done, mark the kitchen queue as done
        if (servers.stream().allMatch(Server::isDone)) {
            orders.setDone();
        }

        // When kitchen is done coffee shop can close
        // Output log, report and notify observers
        if (kitchen.isDone()) {
            Logger.getInstance().writeReport("log.txt");
            new ReportWriter(kitchen.getServedCustomers()).write(new File("report.txt"));
            notifyObservers();
        }
    }
}
