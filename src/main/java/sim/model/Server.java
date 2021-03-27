package sim.model;

import java.util.LinkedList;

import sim.app.Order;
import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Server implements Runnable, Subject {
    private SharedQueue customerQueue, kitchenQueue;
    private Order[] currentOrder = null;
    private LinkedList<Observer> observers;
    private Logger log;
    private Long speed;
    private static final Long BASE = 10000l;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;

        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
        speed = BASE;
    }

    @Override
    public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!customerQueue.getDone() || !customerQueue.isEmpty()) {
            Order [] order = customerQueue.getCustomerOrder();
            setCurrentOrder(order);
            notifyObservers();
        	try {
                // Time to process order depends on number of items
                Thread.sleep(speed * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        	log.add(order, Logger.OrderState.PROCESSED);
        	kitchenQueue.addOrder(order);
        	// clear the order once finished
        	currentOrder = null;
        	notifyObservers();
        }

        kitchenQueue.setDone();
    }

    // adds details of the order being processed by the Server
    public void setCurrentOrder(Order[] o) {
    	currentOrder = o;
    }

    public Order[] getCurrentOrder() {
    	return currentOrder;
    }

    public void setSpeed(Long l) {
    	speed = l;
    	notifyObservers();
    }

    public Long getSpeed() {
    	return speed;
    }

    public Long getBaseSpeed() {
    	return BASE;
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
