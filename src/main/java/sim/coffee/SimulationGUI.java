package sim.coffee;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SimulationGUI extends JFrame implements Observer{
	
	private JTextArea queue1, queue2, priorityQueue;
	private JPanel serverStaff, queueSection, controls;
	private SharedQueue customerQueue, kitchenQueue;
	private int nrOfThreads;
	
	public SimulationGUI(SharedQueue customerQueue, SharedQueue kitchenQueue,  int nrOfThreads) {
		
		this.customerQueue = customerQueue;
		this.kitchenQueue = kitchenQueue;
		this.nrOfThreads = nrOfThreads;
		customerQueue.registerObserver(this);
		kitchenQueue.registerObserver(this);
		
		setTitle("Coffee Shop Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(600, 700));
		setLocationRelativeTo(null);
		
		setup();
		pack();
		setVisible(true);

	}
	
	// sets the overall GUI layout
	private void setup() {
		JPanel main = new JPanel(new GridLayout(4,1));
		
		main.add(queueSection());
		main.add(setupServer());
		main.add(setupQueue2());
		main.add(setupKitchen());
		
		main.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(main, BorderLayout.CENTER);
		//add(main, BorderLayout.WEST);
	}
	
	// sets up the queue with customers waiting to be served 
	private JPanel queueSection()
	{
		queueSection = new JPanel(new GridLayout(1, 2));
		queue1 = new JTextArea();
		queue1.setEditable(false);
		Border qBorder1 = BorderFactory.createTitledBorder("Queue");
		JScrollPane queue1Pane = new JScrollPane(queue1);
		queue1Pane.setBorder(qBorder1);
		
		priorityQueue = new JTextArea();
		priorityQueue.setEditable(false);
		Border qBorder2 = BorderFactory.createTitledBorder("Priority Queue");
		JScrollPane queue3Pane = new JScrollPane(priorityQueue);
		queue3Pane.setBorder(qBorder2);
		
		queueSection.add(queue1Pane);
		queueSection.add(queue3Pane);
		return queueSection;
	}
	
	
	// sets up the server section
	// TODO: will need to adjust GUI width if more than 3 servers are added
	// probably should set a limit to the nr of threads that can be initiated
	private JPanel setupServer() {
		
		serverStaff = new JPanel(new GridLayout(1, nrOfThreads));
		// the number of servers depends on the number of threads specified upon instantiating the GUI
		// in every iteration Server object is created which is then used to start a thread and create a ServerGUI
		// ServerGUI is then added to the main serveStaff JPanel 
		for (int i = 1; i <= nrOfThreads; i++) {
			Server staff = new Server(customerQueue, kitchenQueue);
			Thread thread = new Thread(staff);
			serverStaff.add(new ServerGUI(staff, i));
			thread.start();
		}
		return serverStaff;
	}
	
	// TODO: not working atm, crashes the application when run
	private JPanel setupKitchen() {
		JPanel kitchenSection = new JPanel(new GridLayout(1, 1));

		Kitchen kitchen = new Kitchen(kitchenQueue);
		kitchenSection.add(new KitchenGUI(kitchen));
		Thread kitchenThread = new Thread(kitchen);
		kitchenThread.start();
		
		return kitchenSection;
	}
	
	// ideally this section would be a JScrollPane but adding  KitchenQueueGUI object to JScrollPane 
	// (or making  KitchenQueueGUI a JScrollPane) stops the queue section from updating/ or the GUI eventually just freezes 
	private JTextArea setupQueue2() {
		KitchenQueueGUI queue2Pane = new KitchenQueueGUI(kitchenQueue);
		return queue2Pane;
	}
	
	// this will contain all the interactive elements (e.g. buttons etc)
	private JPanel setupControlls() {
		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
		return controls;
	}
	
	// Observer method
	public void update() {
		LinkedList<Order[]> currentQueue = customerQueue.getQueue();
		
		StringBuilder queueLog = new StringBuilder();
 		queueLog.append("Customers in the queue: ");
 		queueLog.append(currentQueue.size() + "\n");

 		for (Order[] o : currentQueue) {
 			queueLog.append(String.format("%-15s", o[0].getCustomerID()));
 			queueLog.append(o.length);
 			queueLog.append(" Item(s) \n");
 		}
		
		queue1.setText(queueLog.toString());
	}
}
