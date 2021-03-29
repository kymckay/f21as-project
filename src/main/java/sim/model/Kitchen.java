package sim.model;

import java.util.LinkedList;
import java.util.List;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Kitchen implements Runnable, Subject {
	private SharedQueue kitchenQueue;
    private Customer currentCustomer;

    private LinkedList<Observer> observers = new LinkedList<>();
    private Logger log = Logger.getInstance();

    // Served customers will be stored for output report later
    private LinkedList<Customer> completed = new LinkedList<>();

    // Set once server is finished serving
    private boolean done;

	public Kitchen(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
	}

	@Override
	public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!kitchenQueue.getDone() || !kitchenQueue.isEmpty()) {
            currentCustomer = kitchenQueue.getCustomer();
            notifyObservers();

        	try {
                // Time to process order depends on number of items
                Thread.sleep(5000l * currentCustomer.getOrder().length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Log order as completed
            completed.add(currentCustomer);
        	log.add(currentCustomer, Logger.OrderState.SERVED, kitchenQueue.getQueueType());
        }

        // Finish service
        done = true;
        currentCustomer = null;
        notifyObservers();
    }

    public Customer getCurrentCustomer() {
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
