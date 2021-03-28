package sim.views;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.app.Order;
import sim.interfaces.Observer;
import sim.model.SharedQueue;

public class QueueGUI extends JPanel implements Observer {

	SharedQueue queue;
	JTextArea display;

	public QueueGUI(SharedQueue queue, String title) {
		this.queue = queue;
		queue.registerObserver(this);
		setLayout(new GridLayout(1,1)); // ensures JPanel takes up all available space in the GUI
		setup(title);
	}

	public void setup(String title) {
		// Text area provides convenient wrapping
		display = new JTextArea();
		display.setEditable(false);

		// Scroll pane remove limit on content height
		JScrollPane queuePane = new JScrollPane(display);

		// Identify the queue for the user
		Border border = BorderFactory.createTitledBorder(title);
		queuePane.setBorder(border);

		add(queuePane);

		// Fill initial state
		update();
	}

	@Override
	public void update() {
		List<Order[]> currentQueue = queue.getQueue();

		StringBuilder queueLog = new StringBuilder();
 		queueLog.append(String.format("Orders in the queue: %d%n", currentQueue.size()));

 		for (Order[] o : currentQueue) {
 			queueLog.append(String.format("%-15s", o[0].getCustomerID()));
 			queueLog.append(o.length);
 			queueLog.append(" Item(s) \n");
 		}
		display.setText(queueLog.toString());
	}

}
