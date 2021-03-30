package sim.model;

import java.util.LinkedList;
import java.util.Optional;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Server implements Runnable, Subject {
	// Static variable tracks number of servers that exist
	private static int count = 0;
	private int number = 0; // Each new server gets a unique number ID

    private SharedQueue customerQueue;
	private SharedQueue kitchenQueue;
    private LinkedList<Observer> observers;
    private Logger log;
    private long speed;

    // Server is not always serving a customer
    private Optional<Customer> currentCustomer;

    // Default service speed per order item
    public static final long BASE_SPEED = 20000l;

    // Set once server is finished serving
    private boolean done;

    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;

		// Increment server number
		this.number = ++count;

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
        while (!customerQueue.isDone() || !customerQueue.isEmpty()) {
            currentCustomer = customerQueue.getCustomer();

            if (currentCustomer.isPresent()) {
                Customer toServe = currentCustomer.get();

                // Update model state with new customer
                notifyObservers();
				log.add(
					String.format(
						"Server %d starts serving %s",
						number,
						toServe.getName()
					)
				);

                try {
                    // Time to process order depends on number of items
                    Thread.sleep(speed * toServe.getOrder().length);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Pass order on to kitchen queue (only has one lane)
                kitchenQueue.add(toServe, 0);
				log.add(
					String.format(
						"Server %d sends an order to the kitchen for %s",
						number,
						toServe.getName()
					)
				);

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

	public int getNumber() {
		return number;
	}
}
