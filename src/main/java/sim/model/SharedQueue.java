package sim.model;

import java.util.LinkedList;
import java.util.List;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class SharedQueue implements Subject {

	private LinkedList<Customer> queue = new LinkedList<>();
	private LinkedList<Observer> observers = new LinkedList<>();
	private boolean empty = true;
	private boolean done = false;
	private Logger log = Logger.getInstance();
	private QueueType queueType;

	public SharedQueue (QueueType queueType) {
		this.queueType = queueType;

	}

	// returns an array of order at the top of the queue
	public synchronized Customer getCustomer() {
		while (empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		 	}
		}

		Customer customer = queue.getFirst();

		switch (queueType) {
		case CUSTOMER:
			log.add(customer, Logger.OrderState.EXITKITCHEN, QueueType.CUSTOMER);
			break;
		case KITCHEN:
			log.add(customer, Logger.OrderState.EXITKITCHEN, QueueType.KITCHEN);
			break;
		default:
			log.add(customer, Logger.OrderState.EXIT, QueueType.PRIORITY);
			break;
		}

		queue.removeFirst();
		notifyObservers();

		if (queue.isEmpty()) {
			empty = true;
			notifyAll();
		}

		return customer;
	}

	// Add a customer to the back of the queue
	public synchronized void add(Customer c) {
		queue.addLast(c);
		switch (queueType) { //adds an entry in the log every time the method is called
		case CUSTOMER:
			log.add(c, Logger.OrderState.ENTERKITCHEN, QueueType.CUSTOMER);
			break;
		case KITCHEN:
			log.add(c, Logger.OrderState.ENTERKITCHEN, QueueType.KITCHEN);
			break;
		default:
			log.add(c, Logger.OrderState.ENTER, QueueType.PRIORITY);
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


	public synchronized List<Customer> getQueue() {
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
