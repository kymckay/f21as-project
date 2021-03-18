package sim.coffee;

import java.util.LinkedList;

public class Server implements Runnable, Subject {
    private SharedQueue queue;
    private StringBuilder currentOrder;
    private LinkedList<Observer> observers;
    private Logger log;


    public Server(SharedQueue queue) {
        this.queue = queue;
        currentOrder = new StringBuilder();
        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
    }

    @Override
    public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!queue.getDone() || !queue.isEmpty()) {
            Order [] order = queue.getCustomerOrder();
            setCurrentOrder(order);
            notifyObservers();
        	try {
                // Time to process order depends on number of items
                Thread.sleep(10000l * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.writeReport("log.txt");
    }
    
    // adds details of the order being processed by the Server
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
}
