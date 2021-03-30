package sim.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Kitchen implements Runnable, Subject {
	private SharedQueue kitchenQueue;

    // Kitchen is not always serving a customer
    private Optional<Customer> currentCustomer;

    private LinkedList<Observer> observers = new LinkedList<>();
    private Logger log = Logger.getInstance();

    // Served customers will be stored for output report later
    private LinkedList<Customer> completed = new LinkedList<>();

    // Set once server is finished serving
    private boolean done;

	public Kitchen(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;

        // No customer to start with
        currentCustomer = Optional.empty();
	}

	@Override
	public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!kitchenQueue.isDone() || !kitchenQueue.isEmpty()) {
            currentCustomer = kitchenQueue.getCustomer();

            if (currentCustomer.isPresent()) {
                Customer toServe = currentCustomer.get();

                notifyObservers();

                try {
                    // Time to process order depends on number of items
                    Thread.sleep(5000l * toServe.getOrder().length);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Log order as completed
                completed.add(toServe);
                log.add(toServe, Logger.OrderState.SERVED, kitchenQueue.getQueueType());

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

    public List<Customer> getServedCustomers() {
        return completed;
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
		for (Observer o : observers) {
			o.update();
		}
	}

    public boolean isDone() {
        return done;
    }
}
