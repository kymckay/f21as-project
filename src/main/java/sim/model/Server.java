package sim.model;

import java.util.LinkedList;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Server implements Runnable, Subject {
    private SharedQueue customerQueue, kitchenQueue, priorityQueue;
    private Customer currentCustomer;
    private LinkedList<Observer> observers;
    private Logger log;
    private long speed;

    // Default service speed relevant to other classes
    public static final long BASE_SPEED = 10000l;

    // Set once server is finished serving
    private boolean done;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue, SharedQueue priorityQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;
        this.priorityQueue = priorityQueue;

        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
        speed = BASE_SPEED;
    }

    @Override
    public void run() {

        // Process queue from priorityQueue over customerQueue if there are still orders in it.
        // Service continues as long as customers are still due to arrive or customers
        // are in the queue

        while (!customerQueue.getDone() || !customerQueue.isEmpty()
                || !priorityQueue.getDone() || !priorityQueue.isEmpty()) {

            // Prioritise the priority queue
            SharedQueue target = !priorityQueue.isEmpty() ? priorityQueue : customerQueue;

            currentCustomer = target.getCustomer();

            // Update model state with current customer
            notifyObservers();

            try {
                // Time to process order depends on number of items
                Thread.sleep(speed * currentCustomer.getOrder().length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Pass order on to kitchen queue
            log.add(currentCustomer, Logger.OrderState.PROCESSED, target.getQueueType());
            kitchenQueue.add(currentCustomer);

            // Update model state to reflect customer served
            currentCustomer = null;
            notifyObservers();
        }

        // Finish service
        done = true;
        currentCustomer = null;
        notifyObservers();
    }

    public Customer getCurrentCustomer() {
    	return currentCustomer;
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
		// prevents concurrent modification exception if notifyObserver() is called while observers are being added
		LinkedList<Observer> current = (LinkedList)observers.clone();
		current.add(o);
		observers = current;
	}

	// removes observers from a list
	public void removeObserver(Observer o) {
		// prevents concurrent modification exception if notifyObserver() is called while observers are being added
		LinkedList<Observer> current = (LinkedList)observers.clone();
		current.remove(o);
		observers = current;
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
