package sim.model;

import java.util.LinkedList;

import sim.app.Order;
import sim.view.Observer;

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
		case KITCHEN:
			log.add(customerOrder, Logger.OrderState.EXITKITCHEN);
			break;
		default:
			log.add(customerOrder, Logger.OrderState.EXIT);
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
		case KITCHEN:
			log.add(o, Logger.OrderState.ENTERKITCHEN);
			break;
		default:
			log.add(o, Logger.OrderState.ENTER);
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


	public synchronized LinkedList<Order[]> getQueue() {
 		return queue;
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

	public QueueType getQueueType() {
		return queueType;
	}
}
