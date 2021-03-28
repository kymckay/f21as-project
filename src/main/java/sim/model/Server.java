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
    private long speed;

    // Default service speed relevant to other classes
    public static final long BASE_SPEED = 10000l;

    // Set once server is finished serving
    private boolean done;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;

        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
        speed = BASE_SPEED;
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

        // Finish service
        done = true;
        currentOrder = null;
        notifyObservers();
    }

    // adds details of the order being processed by the Server
    public void setCurrentOrder(Order[] o) {
    	currentOrder = o;
    }

    public Order[] getCurrentOrder() {
    	return currentOrder;
    }

    public void setSpeed(long l) {
    	speed = l;
    	notifyObservers();
    }

    public long getSpeed() {
    	return speed;
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

    public boolean isDone() {
        return done;
    }
}
