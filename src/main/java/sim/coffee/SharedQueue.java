package sim.coffee;

import java.util.LinkedList;

public class SharedQueue implements Subject {

	private LinkedList<Order[]> queue = new LinkedList<>();
	private LinkedList<Observer> observers = new LinkedList<>();
	private boolean empty = true;
	private boolean done = false;
	private Logger log = Logger.getInstance();
	private QueueType queueType;
	
	public SharedQueue (QueueType queueType) {
		this.queueType = queueType;
		
	}

	// returns an array of order at the top of the queue
	public synchronized Order[] getCustomerOrder() {
		while (empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		 	}
		}

		Order[] customerOrder = queue.getFirst();
		
		switch (queueType) {
		case CUSTOMER:
			log.add(customerOrder, Logger.OrderState.EXIT);
			break;
		case KITCHEN:
			log.add(customerOrder, Logger.OrderState.EXITKITCHEN);
			break;
		}
		
		queue.removeFirst();
		notifyObservers();

		if (queue.isEmpty()) {
			empty = true;
			notifyAll();
		}

		return customerOrder;
	}

	// adds an array of orders to the queue
	public synchronized void addOrder(Order[] o) {
		queue.addLast(o);
		switch (queueType) { //adds an entry in the log every time the method is called
		case CUSTOMER:
			log.add(o, Logger.OrderState.ENTER);
			break;
		case KITCHEN:
			log.add(o, Logger.OrderState.ENTERKITCHEN);
			break;
		}
		 
		empty = false;
		notifyAll();
		notifyObservers();
	}

	public boolean getDone() {
		return done;
	}

	public void setDone() {
		done = true;
	}

	
	public synchronized StringBuilder getQueue() {
		StringBuilder queueLog = new StringBuilder();
 		queueLog.append("Customers in the queue: ");
 		queueLog.append(queue.size() + "\n");

 		for (Order[] o : queue) {
 			queueLog.append(String.format("%-15s", o[0].getCustomerID()));
 			queueLog.append(o.length);
 			queueLog.append(" Item(s) \n");
 		}
 		return queueLog;

	}


	public boolean isEmpty() {
		return empty;
	}

	// implement Observer pattern
	// adds observers to a list
	public void registerObserver(Observer o) {
		observers.add(o);
		
	}

	// removes observers from a list 
	public void removeObserver(Observer o) {
		observers.remove(o);
		
	}

	// notify all observers in the observers list 
	public void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
		
	}
}
