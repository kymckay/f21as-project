package sim.views;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import sim.interfaces.Observer;
import sim.model.CoffeeShop;
import sim.model.Server;

public class SimulationGUI extends JFrame implements Observer {

	private CoffeeShop coffeeShop;

	private JButton addServer;
	private JButton removeServer;
	private JPanel serverStaff;

	// List of sub-views controllers need access to
	private LinkedList<ServerGUI> staffViews = new LinkedList<>();

	public SimulationGUI(CoffeeShop coffeeShop) {
		this.coffeeShop = coffeeShop;
		coffeeShop.registerObserver(this);

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
		JPanel main = new JPanel(new GridLayout(5,1));

		main.add(queueSection());
		main.add(setupServer());
		main.add(setupQueue2());
		main.add(setupKitchen());
		main.add(setupControlls());

		main.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(main, BorderLayout.CENTER);
	}

	// sets up the queue with customers waiting to be served
	private JPanel queueSection() {
		JPanel queueSection = new JPanel(new GridLayout(1, 2));

		queueSection.add(new QueueGUI(coffeeShop.getCustomers(), "Regular Queue"));
		queueSection.add(new QueueGUI(coffeeShop.getPriorityCustomers(), "Priority Queue"));
		return queueSection;
	}


	// sets up the server section
	// TODO: will need to adjust GUI width if more than 3 servers are added
	// probably should set a limit to the nr of threads that can be initiated
	private JPanel setupServer() {
		serverStaff = new JPanel(new GridLayout(1, 0));

		// Populate server section with a view for each server in the shop
		for (Server s : coffeeShop.getServers()) {
			ServerGUI view = new ServerGUI(s);
			serverStaff.add(view);
			staffViews.add(view);
		}

		return serverStaff;
	}

	public void updateServer(String cond) {
		Server last = coffeeShop.getServers().getLast();
		if (cond == "add") {
			staffViews.add(new ServerGUI(last));
			serverStaff.add(new ServerGUI(last));
		} else {
			staffViews.removeLast();
			// TODO: remove serverStaff
		}
	}

	private JPanel setupKitchen() {
		JPanel kitchenSection = new JPanel(new GridLayout(1, 1));
		kitchenSection.add(new KitchenGUI(coffeeShop.getKitchen()));
		return kitchenSection;
	}

	// sets up kitchen queue
	private JPanel setupQueue2() {
		return new QueueGUI(coffeeShop.getOrders(), "Kitchen Queue");
	}

	// this will contain all the interactive elements (e.g. buttons etc)
	private JPanel setupControlls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.setAlignmentX(Component.CENTER_ALIGNMENT);
		addServer = new JButton("Add Server");
		removeServer = new JButton("Remove Server");
		controls.add(addServer);
		controls.add(removeServer);
		return controls;
	}

	public void addAddServerListener(ActionListener e) {
		addServer.addActionListener(e);
	}

	public void addRemoveServerListener(ActionListener e) {
		removeServer.addActionListener(e);
	}

	// Observes when shop is finished simulation
	public void update() {
		JOptionPane.showMessageDialog(this, "The simulation has now ended. \nEnd of day report and log saved to \"report.txt\" and \"log.txt\".");
		this.dispose();
	}

	public List<ServerGUI> getStaffViews() {
		return staffViews;
	}
}
