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

    /**
     * Run only while its not done and queue isn't empty
     */
    @Override
    public void run() {
        while (!queue.getDone()) {

        	Order [] order = queue.getCustomerOrder();
        	setCurrentOrder(order);
        	notifyObservers();
            try {

                // Tries to simulate no. of orders * time it take to prepare one order
                int orderSize = order.length;
                Thread.sleep(10000 * orderSize);

            } catch (InterruptedException e) {}
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
