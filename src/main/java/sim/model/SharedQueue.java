package sim.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import sim.interfaces.Observer;
import sim.interfaces.Subject;

public class SharedQueue implements Subject {
	// Queue can consist of multiple lanes (prioritised by lower index)
	// Prioritisation must be done in this shared class to avoid consumers waiting
	// on one queue when another has customers (if multiple instances were used)
	private ArrayList<LinkedList<Customer>> lanes;

    // Observers register themselves here for notifications
	private LinkedList<Observer> observers = new LinkedList<>();

    // Flags for producer/consumers to check queue state
    private boolean empty = true;
    private boolean done = false;

	public SharedQueue(int numLanes) {
		if (numLanes <= 0) {
			throw new IllegalArgumentException("Number of queue lanes must be a positive integer");
		}

		lanes = new ArrayList<>(numLanes);

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new LinkedList<>());
		}
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

		// Checking empty flag should garantee there's always a customer in one of the
		// lanes, but we don't want to assume
		Optional<Customer> customer = Optional.empty();

		// Earlier index lanes are prioritised
		for (LinkedList<Customer> list : lanes) {
			if (!list.isEmpty()) {
				customer = Optional.of(list.removeFirst());
				break;
			}
		}

        // No need to notify threads as producer never needs to wait
		if (lanes.stream().allMatch(List::isEmpty)) {
			empty = true;
		}

        notifyObservers();

		return customer;
	}

	// Add a customer to the back of the queue
	public synchronized void add(Customer c, int lane) {
        // Enforce setDone call after last customer is added
        if (done) throw new IllegalStateException("Cannot add to a completed queue");

		// Lane must exist
		if ((lane < 0) || (lane >= lanes.size())) {
			throw new IllegalArgumentException("Cannot add to non-existent queue lane " + lane);
		}

        // Producer never needs to wait since we're dealing with a queue
		lanes.get(lane).addLast(c);
        empty = false;

        // Inform all consumers and observers that the queue is no longer empty
		notifyAll();
		notifyObservers();
	}

	public boolean isDone() {
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

	/**
	 * Returns a copy of a queue lane. Useful for observers. The copy is to avoid
	 * concurrent modification errors if the caller wants to iterate over the
	 * object.
	 *
	 * Method must be synchronized since the copy constructor iterates the
	 * collection to build the new one (i.e. is not atomic)
	 *
	 * @param lane index of lane to get
	 * @return a copy of the queue lane (list of customers)
	 */
	public synchronized List<Customer> getLane(int lane) {
 		return new LinkedList<>(lanes.get(lane));
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public void registerObserver(Observer o) {
		// Synchronized to avoid concurrent modifications while notifying
		// Use of observers as lock object since this doesn't need to block the main
		// queue methods
		synchronized(observers) {
			observers.add(o);
		}
	}

	@Override
	public void removeObserver(Observer o) {
		// Synchronized to avoid concurrent modifications while notifying
		// Use of observers as lock object since this doesn't need to block the main
		// queue methods
		synchronized(observers) {
			observers.remove(o);
		}
	}

	@Override
	public void notifyObservers() {
		// Synchronized to avoid concurrent modifications while notifying
		// Use of observers as lock object since this doesn't need to block the main
		// queue methods
		synchronized(observers) {
			for (Observer o : observers) o.update();
		}
	}
}
