package sim.coffee;

import java.util.LinkedList;

public class Server implements Runnable, Subject {
    private SharedQueue customerQueue, kitchenQueue;
    private Order[] currentOrder;
    private LinkedList<Observer> observers;
    private Logger log;


    public Server(SharedQueue customerQueue, SharedQueue kitchenQueue) {
        this.customerQueue = customerQueue;
        this.kitchenQueue = kitchenQueue;
        
        observers = new LinkedList<Observer>();
        log = Logger.getInstance();
    }

    @Override
    public void run() {

        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!customerQueue.getDone() || !customerQueue.isEmpty()) {
            Order [] order = customerQueue.getCustomerOrder();
            setCurrentOrder(order);
            notifyObservers();
        	try {
                // Time to process order depends on number of items
                Thread.sleep(10000l * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        	log.add(order, Logger.OrderState.PROCESSED);
        	kitchenQueue.addOrder(order);
        }
        currentOrder = null;
        notifyObservers();
        
        kitchenQueue.setDone();
        log.writeReport("log.txt");
    }
    
    // adds details of the order being processed by the Server
    public void setCurrentOrder(Order[] o) {
    	currentOrder = o;
    }
    
    public Order[] getCurrentOrder() {
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
