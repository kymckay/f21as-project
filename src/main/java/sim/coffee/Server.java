package sim.coffee;

import java.util.LinkedList;

public class Server implements Runnable, Subject {
    private SharedQueue queue;
    private StringBuilder currentOrder;
    private LinkedList<Observer> observers;

    public Server(SharedQueue queue) {
        this.queue = queue;
        currentOrder = new StringBuilder();
        observers = new LinkedList<Observer>();
        
    }

    /**
     * Run only while its not done and queue isn't empty
     */
    @Override
    public void run() {
        while (!queue.getDone()) {
        	Order [] order = queue.getCustomerOrder();
        	setCurrentOrder(order);
            try {
                // Tries to simulate no. of orders * time it take to prepare one order
                int orderSize = order.length;
                Thread.sleep(10000 * orderSize);
            } catch (InterruptedException e) {}
        }
    }
    
    public void setCurrentOrder(Order[] o) {
    	currentOrder.replace(0, currentOrder.length(), "");
    	currentOrder.append("Customer being served: ");
    	currentOrder.append(o[0].getCustomerID() + "\n");
    	currentOrder.append("Customer being served: ");
    	currentOrder.append("Orders: \n");
    	for (Order order : o) {
    		currentOrder.append(String.format("%10s", order.getItemId()) + "\n");
    	}
    }
    
    public StringBuilder getCurrentOrder() {
    	return currentOrder;
    }

    // implements Observer pattern
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		observers.remove(o);	
	}

	public void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
		
	}
}
