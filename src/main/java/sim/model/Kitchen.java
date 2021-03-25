package sim.model;

import java.util.LinkedList;

import sim.app.Order;
import sim.view.Observer;

public class Kitchen implements Runnable, Subject {
	private SharedQueue kitchenQueue;
    private Order[] currentOrder;
    private LinkedList<Observer> observers;
    private Logger log = Logger.getInstance();

    // Completed orders will be stored for output report later
    private LinkedList<Order[]> completed = new LinkedList<>();

    // Set once server is finished serving
    private boolean done;

	public Kitchen(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
		observers = new LinkedList<>();
	}

	@Override
	public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!kitchenQueue.getDone() || !kitchenQueue.isEmpty()) {
            Order[] order = kitchenQueue.getCustomerOrder();
            setCurrentOrder(order);
            notifyObservers();
        	try {
                // Time to process order depends on number of items
                Thread.sleep(5000l * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Log order as completed
            completed.add(order);
        	log.add(order, Logger.OrderState.SERVED);
        }

        // Finish service
        done = true;
        currentOrder = null;
        notifyObservers();
    }

    // adds details of the order being processed by the Kitchen
    public void setCurrentOrder(Order[] o) {
    	currentOrder = o;
    }

    public Order[] getCurrentOrder() {
    	return currentOrder;
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
