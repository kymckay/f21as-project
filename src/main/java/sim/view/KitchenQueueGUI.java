package sim.view;

import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.app.Order;
import sim.model.SharedQueue;

public class KitchenQueueGUI extends JPanel implements Observer{

	SharedQueue kitchenQueue;
	JTextArea queue;

	public KitchenQueueGUI(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
		kitchenQueue.registerObserver(this);
		setLayout(new GridLayout(1,1)); // ensures JPanel takes up all available space in the GUI
		setup();
	}

	public void setup() {
		queue = new JTextArea();
		queue.setEditable(false);
		queue.setText("Customers in the queue: 0");
		Border border = BorderFactory.createTitledBorder("Kitchen Queue");
		JScrollPane queuePane = new JScrollPane(queue);
		queuePane.setBorder(border);
		add(queuePane);
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
		queue.setText(queueLog.toString());
	}

}
