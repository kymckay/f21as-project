package sim.model;

import java.util.LinkedList;
import java.util.Optional;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Server implements Runnable, Subject {
    private SharedQueue customerQueue, kitchenQueue, priorityQueue;
    private LinkedList<Observer> observers;
    private Logger log;
    private long speed;

    // Server is not always serving a customer
    private Optional<Customer> currentCustomer;

    // Default service speed relevant to other classes
    public static final long BASE_SPEED = 10000l;

    // Set once server is finished serving
    private boolean done;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue, SharedQueue priorityQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;
        this.priorityQueue = priorityQueue;

        observers = new LinkedList<>();
        log = Logger.getInstance();
        speed = BASE_SPEED;

        // No customer to start with
        currentCustomer = Optional.empty();
    }

    @Override
    public void run() {
        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (
            !customerQueue.isDone() || !priorityQueue.isDone()
            || !customerQueue.isEmpty() || !priorityQueue.isEmpty()
        ) {
            // Prioritise the priority queue
            SharedQueue target = !priorityQueue.isEmpty() ? priorityQueue : customerQueue;

            currentCustomer = target.getCustomer();

            if (currentCustomer.isPresent()) {
                Customer toServe = currentCustomer.get();

                // Update model state with new customer
                notifyObservers();

                try {
                    // Time to process order depends on number of items
                    Thread.sleep(speed * toServe.getOrder().length);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Pass order on to kitchen queue
                log.add(toServe, Logger.OrderState.PROCESSED, target.getQueueType());
                kitchenQueue.add(toServe);

                // Update model state to reflect customer served
                currentCustomer = Optional.empty();
                notifyObservers();
            }
        }

        // Finish service
        done = true;
        notifyObservers();
    }

    public Optional<Customer> getCurrentCustomer() {
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
