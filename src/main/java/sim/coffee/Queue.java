package sim.coffee;

import java.util.LinkedList;

public class Queue {
	
	private LinkedList<Order> queue;
	private boolean empty;
	private boolean done;
	
	public Queue() {
		queue = new LinkedList<Order>();
		empty = true;
		done = false;
	}
	
	// return customer id at the top of the queue + details for all orders with the same customer id 
	// assume that the same customer is given a different customer IDs for different purchases
	// a purchase can include multiple items (e.g. orders)
	public synchronized StringBuilder getCustomerOrder() {
		while (empty) {
			try {
				wait();
			}
		 	catch (InterruptedException e) {
		 		e.printStackTrace();
		 	}	
		}
		
		StringBuilder customerOrder = new StringBuilder();
		// depending on what we include in the order.csv, this could also be the name of the customer  
		customerOrder.append(queue.getFirst().getCustomerID() + "\n");
		 
		String custID = queue.getFirst().getCustomerID();
		LinkedList<Order> toRemove = new LinkedList<Order>(); 
		// adds all orders with the same customer id to string
		for(Order order : queue) {
			if(order.getCustomerID().equals(custID)) {
				customerOrder.append(order.getItemDetails() + "\n");
				toRemove.add(order);
			}
		}
		// removes all orders with the customer id matching the one on top of the queue 
		queue.removeAll(toRemove);
		toRemove.clear();
		
		if (queue.size() == 0) {
			empty = true;
			notifyAll();
		}
		return customerOrder;
	}
	
	// adds orders to the queue
	// assume simultaneous addition of multiple orders with the same customer id is handled in producer class
	public synchronized void addOrder(Order o) {
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
	
//	// displays queue for GUI?
// 	public String displayQueue() {
// 		 
// 		return queue.toString();
// 	}

}
