package sim.view;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import sim.app.Order;
import sim.controller.SpeedController;
import sim.interfaces.Observer;
import sim.model.CoffeeShop;
import sim.model.Server;

public class SimulationGUI extends JFrame implements Observer {

	private CoffeeShop coffeeShop;

	private JTextArea queue1;
	private JTextArea queue2;
	private JTextArea priorityQueue;

	public SimulationGUI(CoffeeShop coffeeShop) {
		this.coffeeShop = coffeeShop;

		coffeeShop.getCustomers().registerObserver(this);
		coffeeShop.getOrders().registerObserver(this);

		setTitle("Coffee Shop Simulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(600, 800));
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
	}

	// sets up the queue with customers waiting to be served
	private JPanel queueSection() {
		JPanel queueSection = new JPanel(new GridLayout(1, 2));
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
		JPanel serverStaff = new JPanel(new GridLayout(1, 0));
		//serverStaff.add(new JLabel("Change the serving speed for each server: "));


		// Populate server section with a view for each server in the shop
		for (Server s : coffeeShop.getServers()) {

			ServerGUI gui = new ServerGUI(s);
			SpeedController controls = new SpeedController(s, gui);
			serverStaff.add(gui);
		}

		return serverStaff;
	}

	// TODO: not working atm, crashes the application when run
	private JPanel setupKitchen() {
		JPanel kitchenSection = new JPanel(new GridLayout(1, 1));
		kitchenSection.add(new KitchenGUI(coffeeShop.getKitchen()));
		return kitchenSection;
	}

	// sets up kitchen queue
	private JPanel setupQueue2() {
		return new KitchenQueueGUI(coffeeShop.getOrders());
	}

	// this will contain all the interactive elements (e.g. buttons etc)
	private JPanel setupControlls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
		return controls;
	}

	// Observer method
	public void update() {
		LinkedList<Order[]> currentQueue = coffeeShop.getCustomers().getQueue();

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
