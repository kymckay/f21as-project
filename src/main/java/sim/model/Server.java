package sim.model;

import java.util.LinkedList;

import sim.app.Order;
import sim.view.Observer;

public class Server implements Runnable, Subject {
    private SharedQueue customerQueue, kitchenQueue, priorityQueue;
    private Order[] currentOrder;
    private LinkedList<Observer> observers;
    private Logger log;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue, SharedQueue priorityQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;
        this.priorityQueue = priorityQueue;

        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
    }

    public void threadAction(Order [] o, QueueType q) {
        setCurrentOrder(o);
        notifyObservers();
        try {
            // Time to process order depends on number of items
            Thread.sleep(10000l * o.length);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.add(o, Logger.OrderState.PROCESSED, q);
        kitchenQueue.addOrder(o);
    }

    @Override
    public void run() {

        // Process queue from priorityQueue over customerQueue if there are still orders in it. 
        // Service continues as long as customers are still due to arrive or customers
        // // are in the queue
        while (!customerQueue.getDone() || !customerQueue.isEmpty()
                || !priorityQueue.getDone() || !priorityQueue.isEmpty()) {
            
            // If orders still exist in the priority queue
            if (!priorityQueue.isEmpty()) {
                Order [] order = priorityQueue.getCustomerOrder();
                threadAction(order, priorityQueue.getQueueType());
            } else {
                Order [] order = customerQueue.getCustomerOrder();
                threadAction(order, customerQueue.getQueueType());
            }
        }

        // Finish service
        currentOrder = null;
        notifyObservers();

        kitchenQueue.setDone();
        log.writeReport("log.txt");
    }

    // adds details of the order being processed by the Server
    public void setCurrentOrder(Order[] o) {
    	currentOrder = o;
    }

    public Order[] getCurrentOrder() {
    	return currentOrder;
    }

    // Subject methods
    // add observers to a list
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	// removes observers from a list
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	// notifies all observers in the observers list
	public void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}
}
