package sim.coffee;

import java.util.LinkedList;

public class SharedQueue {

	private LinkedList<Order[]> queue;
	private boolean empty;
	private boolean done;

	public SharedQueue() {
		queue = new LinkedList<Order[]>();
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
	}

	public boolean getDone() {
		return done;
	}

	public void setDone() {
		done = true;
	}
	// displays queue for the GUI
	public StringBuilder displayQueue() {
		StringBuilder queueLog = new StringBuilder();
 		queueLog.append("Customers in the queue: ");
 		queueLog.append(queue.size() + "\n");

 		for (Order[] o : queue) {
 			queueLog.append(String.format("%-10s", o[0].getCustomerID()));
 			queueLog.append(o.length);
 			queueLog.append(" Item(s) \n");
 		}
 		return queueLog;
 	}

    public void addOrder(Object[] array) {
    }
	
}
