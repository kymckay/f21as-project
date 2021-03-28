package sim.model;

import java.util.LinkedList;
import java.util.List;

import sim.app.Order;
import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class Kitchen implements Runnable, Subject {
	private SharedQueue kitchenQueue;
    private Order[] currentOrder;

    private LinkedList<Observer> observers = new LinkedList<>();
    private Logger log = Logger.getInstance();

    // Completed orders will be stored for output report later
    private LinkedList<Order[]> completed = new LinkedList<>();

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
            currentOrder = kitchenQueue.getCustomerOrder();
            notifyObservers();

        	try {
                // Time to process order depends on number of items
                Thread.sleep(5000l * currentOrder.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Log order as completed
            completed.add(currentOrder);
        	log.add(currentOrder, Logger.OrderState.SERVED, kitchenQueue.getQueueType());
        }

        // Finish service
        done = true;
        currentOrder = null;
        notifyObservers();
    }

    public Order[] getCurrentOrder() {
    	return currentOrder;
    }

    public List<Order[]> getCompletedOrders() {
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
