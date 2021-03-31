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
    private SharedQueue customers = new SharedQueue(2);
    private SharedQueue orders = new SharedQueue(1);

    // List of registered observers for observer/subject pattern
    private LinkedList<Observer> observers = new LinkedList<>();

    // List of staff are observable
    private LinkedList<Server> servers = new LinkedList<>();

    // Kitchen is observable
    private Kitchen kitchen = new Kitchen(orders);

    public CoffeeShop(Menu menu, int numStaff) {
		Logger.getInstance().add("Simulation initiated");

        // Producer inserts input file of orders into shared queue for staff
        Thread producer = new Thread(new Producer(
            new File("data/orders.csv"),
            customers,
            menu
        ));

        producer.start();

        // Staff members consumes the queue of customer orders
        for (int i = 0; i < numStaff; i++) {
            // Track the staff members
            Server staff = new Server(customers, orders);
            servers.add(staff);

            // Observe staff to later check when service has stopped
            staff.registerObserver(this);

            // Start service
            Thread staffT = new Thread(staff);
            staffT.start();
        }

        // Observe kitchen to later check when service has stopped
        kitchen.registerObserver(this);

        Thread kitchenT = new Thread(kitchen);
        kitchenT.start();
    }

    public SharedQueue getCustomers() {
        return customers;
    }

    public SharedQueue getOrders() {
        return orders;
    }

    public List<Server> getServers() {
        return servers;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    @Override
    public void registerObserver(Observer o) {
		// Synchronized to avoid concurrent modifications while notifying
		synchronized(observers) {
			observers.add(o);
		}
    }

    @Override
    public void removeObserver(Observer o) {
		// Synchronized to avoid concurrent modifications while notifying
		synchronized(observers) {
			observers.remove(o);
		}
    }

    @Override
    public void notifyObservers() {
		// Synchronized to avoid concurrent modifications while notifying
		synchronized(observers) {
			for (Observer o : observers) o.update();
		}
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
			Logger.getInstance().add("Simulation completed");
            Logger.getInstance().writeReport("log.txt");
            new ReportWriter(kitchen.getServedCustomers()).write(new File("report.txt"));
            notifyObservers();
        }
    }
}
