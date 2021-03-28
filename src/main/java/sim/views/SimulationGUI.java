package sim.views;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import sim.interfaces.Observer;
import sim.model.CoffeeShop;
import sim.model.Server;

public class SimulationGUI extends JFrame implements Observer {

	private CoffeeShop coffeeShop;

	// List of sub-views controllers need access to
	private List<ServerGUI> staffViews = new LinkedList<>();

	public SimulationGUI(CoffeeShop coffeeShop) {
		this.coffeeShop = coffeeShop;

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

		queueSection.add(new QueueGUI(coffeeShop.getCustomers()));
		queueSection.add(new QueueGUI(coffeeShop.getPriorityCustomers()));
		return queueSection;
	}


	// sets up the server section
	// TODO: will need to adjust GUI width if more than 3 servers are added
	// probably should set a limit to the nr of threads that can be initiated
	private JPanel setupServer() {
		JPanel serverStaff = new JPanel(new GridLayout(1, 0));

		// Populate server section with a view for each server in the shop
		for (Server s : coffeeShop.getServers()) {
			ServerGUI view = new ServerGUI(s);
			serverStaff.add(view);
			staffViews.add(view);
		}

		return serverStaff;
	}

	private JPanel setupKitchen() {
		JPanel kitchenSection = new JPanel(new GridLayout(1, 1));
		kitchenSection.add(new KitchenGUI(coffeeShop.getKitchen()));
		return kitchenSection;
	}

	// sets up kitchen queue
	private JPanel setupQueue2() {
		return new QueueGUI(coffeeShop.getOrders());
	}

	// this will contain all the interactive elements (e.g. buttons etc)
	private JPanel setupControlls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
		return controls;
	}

	// Observes when shop is finished simulation
	public void update() {
		// TODO
	}

	public List<ServerGUI> getStaffViews() {
		return staffViews;
	}
}
