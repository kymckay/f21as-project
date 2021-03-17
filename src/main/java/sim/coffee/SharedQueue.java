package sim.coffee;

import java.util.LinkedList;

public class SharedQueue implements Subject {

	private LinkedList<Order[]> queue;
	private LinkedList<Observer> observers;
	private boolean empty;
	private boolean done;

	public SharedQueue() {
		queue = new LinkedList<Order[]>();
		observers = new LinkedList<Observer>();
		empty = true;
		done = false;
		
	}

	// returns an array of order at the top of the queue
	public synchronized Order[] getCustomerOrder() {
		while (empty) {
			try {
				wait();
			}
		 	catch (InterruptedException e) {
		 		e.printStackTrace();
		 	}
		}

		Order[] customerOrder = queue.getFirst();
		queue.removeFirst();
		notifyObservers();

		if (queue.size() == 0) {
			empty = true;
			notifyAll();
		}
		
		return customerOrder;
	}

	// adds an array of orders to the queue
	public synchronized void addOrder(Order[] o) {
		queue.addLast(o);
		
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

	
	public StringBuilder getQueue() {
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
