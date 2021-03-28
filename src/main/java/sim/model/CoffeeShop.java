package sim.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import sim.view.Observer;

public class CoffeeShop implements Subject {
    // Queue of orders populated by producer for staff to serve
    // Staff then populate the kitchen queue
    private SharedQueue customers = new SharedQueue(QueueType.CUSTOMER);
    private SharedQueue orders = new SharedQueue(QueueType.KITCHEN);
    private SharedQueue priorityOrders = new SharedQueue(QueueType.PRIORITY);

    // List of registered observers for observer/subject pattern
    private LinkedList<Observer> observers = new LinkedList<>();

    // List of staff are observable
    private LinkedList<Server> servers = new LinkedList<>();

    // Kitchen is observable
    private Kitchen kitchen = new Kitchen(orders);

    public CoffeeShop(int numStaff) {
        // Producer inserts input file of orders into shared queue for staff
        Thread producer = new Thread(new Producer(new File("data/orders.csv"), customers));
        Thread priorityProducer = new Thread(new Producer(new File("data/orders.csv"), customers));

        // Staff members consumes the queue of customer orders
        for (int i = 0; i < numStaff; i++) {
            // Track the staff members
            Server staff = new Server(customers, orders, priorityOrders);
            servers.add(staff);

            // Start service
            Thread staffT = new Thread(staff);
            staffT.start();
        }

        Thread kitchenT = new Thread(kitchen);

        producer.start();
        priorityProducer.start();
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
}
