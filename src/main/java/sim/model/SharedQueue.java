package sim.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class SharedQueue implements Subject {
    // The underlying shared object
	private LinkedList<Customer> queue = new LinkedList<>();

    // Queue logs entry/exit events
    private Logger log = Logger.getInstance();
    private QueueType queueType;

    // Observers register themselves here for notifications
	private LinkedList<Observer> observers = new LinkedList<>();

    // Flags for producer/consumers to check object state
    private boolean empty = true;
    private boolean done = false;

	public SharedQueue (QueueType queueType) {
		this.queueType = queueType;
	}

	// Returns a customer from the top of the queue (once/if there is one)
	public synchronized Optional<Customer> getCustomer() {
		// Wait as long as more orders are to come and the queue is empty
		while (empty && !done) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		 	}
		}

		// If multiple threads want to use the queue it might be done by the time the
		// later threads get here
		if (empty) {
			return Optional.empty();
		}

		Customer customer = queue.removeFirst();

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

        // No need to notify threads as producer never needs to wait
		if (queue.isEmpty()) {
			empty = true;
		}

        notifyObservers();

		return Optional.of(customer);
	}

	// Add a customer to the back of the queue
	public synchronized void add(Customer c) {
        // Enforce setDone call after last customer is added
        if (done) throw new IllegalStateException("Cannot add to a completed queue");

        // Producer never needs to wait since we're dealing with a queue
		queue.addLast(c);
        empty = false;

        // Log every addition to the queue
		switch (queueType) {
            case CUSTOMER:
                log.add(c, Logger.OrderState.ENTERKITCHEN, QueueType.CUSTOMER);
                break;
            case KITCHEN:
                log.add(c, Logger.OrderState.ENTERKITCHEN, QueueType.KITCHEN);
                break;
            default:
                log.add(c, Logger.OrderState.ENTER, QueueType.PRIORITY);
		}

        // Inform all consumers and observers that the queue is no longer empty
		notifyAll();
		notifyObservers();
	}

	public boolean getDone() {
		return done;
	}

    /**
     * Marks the queue as no longer being populated.
     *
     * The method is synchronized as there's no guarantee it would execute before
     * any awoken consumers (who could then re-enter the waiting state indefinitely
     * if the queue is already empty before this runs)
     */
	public synchronized void setDone() {
		done = true;

        // Re-awake any waiting consumers now the queue is done
        notifyAll();
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
