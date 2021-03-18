package sim.coffee;

import java.util.LinkedList;

public class Kitchen implements Runnable, Subject {
	private SharedQueue kitchenQueue;
    private StringBuilder currentOrder;
    private LinkedList<Observer> observers;
	private Logger log;
	
	public Kitchen(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
		currentOrder = new StringBuilder();
		observers = new LinkedList<>();
	}

	@Override
	public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!kitchenQueue.getDone() || !kitchenQueue.isEmpty()) {
            Order [] order = kitchenQueue.getCustomerOrder();
            setCurrentOrder(order);
            notifyObservers();
        	try {
                // Time to process order depends on number of items
                Thread.sleep(5000l * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        	log.add(order, Logger.OrderState.SERVED);
        }
        currentOrder.replace(0, currentOrder.length(), "");
        notifyObservers();
        log.writeReport("log.txt");
    }
		
    // adds details of the order being processed by the Kitchen
    public void setCurrentOrder(Order[] o) {
    	currentOrder.replace(0, currentOrder.length(), "");
    	currentOrder.append("Customer being served: \n");
    	currentOrder.append(String.format("%10s", o[0].getCustomerID()) + "\n");
    	currentOrder.append("Ordered items: \n");
    	for (Order order : o) {
    		currentOrder.append(String.format("%10s", order.getItemId()) + "\n");
    	}
    }
    
    public StringBuilder getCurrentOrder() {
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
}
