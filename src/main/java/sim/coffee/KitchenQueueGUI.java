package sim.coffee;

import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class KitchenQueueGUI extends JTextArea implements Observer{
	
	SharedQueue kitchenQueue;
	
	public KitchenQueueGUI(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
		kitchenQueue.registerObserver(this);
		setup();	
	}
	
	public void setup() {
		setEditable(false);
		Border border = BorderFactory.createTitledBorder("Kitchen Queue");
		setBorder(border);
	}

	@Override
	public void update() {
		LinkedList<Order[]> currentQueue = kitchenQueue.getQueue();
		
		StringBuilder queueLog = new StringBuilder();
 		queueLog.append("Customers in the queue: ");
 		queueLog.append(currentQueue.size() + "\n");

 		for (Order[] o : currentQueue) {
 			queueLog.append(String.format("%-15s", o[0].getCustomerID()));
 			queueLog.append(o.length);
 			queueLog.append(" Item(s) \n");
 		}
		setText(queueLog.toString());		
	}

}
