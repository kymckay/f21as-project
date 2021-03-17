package sim.coffee;

import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.GridLayout;

import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SimulationGUI extends JFrame implements Observer{
	
	private JTextArea queue1, queue2;
	private JPanel serverStaff;
	private SharedQueue queue;
	
	public SimulationGUI(SharedQueue queue) {
		
		this.queue = queue;
		queue.registerObserver(this);
		
		setTitle("Coffee Shop Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(600, 700));
		setLocationRelativeTo(null);
		
		setup();
		pack();
		setVisible(true);
		update();
	}
	
	private void setup() {
		JPanel main = new JPanel(new GridLayout(4,1));
		main.add(setupQueue1());
		main.add(setupServer(3));
		main.add(setupQueue2());
		//main.add(setupKitchenStaff());
		
		main.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(main, BorderLayout.CENTER);
	}
	
	private JScrollPane setupQueue1() {
		
		queue1 = new JTextArea();
		queue1.setEditable(false);
		JScrollPane queue1Pane = new JScrollPane(queue1);
	
		return queue1Pane;
	}
	
	// the number of JPanels depends on the number of threads
	private JPanel setupServer(int i) {
		serverStaff = new JPanel(new GridLayout(1,i));
		
		for (int x = 1; x <= i; x++) {
			serverStaff.add(server(x));
		}
		
		return serverStaff;
	}
	
	private JTextArea server(int i) {
		
		JTextArea server = new JTextArea();
		server.setEditable(false);
		String borderTitle = "Server " + i;
		Border border1 = BorderFactory.createTitledBorder(borderTitle);
		
		server.setBorder(border1);
		return server;
	}
	
	private JScrollPane setupQueue2() {
		
		queue2 = new JTextArea();
		queue2.setEditable(false);
		JScrollPane queue2Pane = new JScrollPane(queue2);

		return queue2Pane;
	}


	
//	private JPanel setupKitchenStaff() {
//		server = new JPanel(new GridLayout(1,2));
//		
//		JTextArea server1 = new JTextArea();
//		server1.setEditable(false);
//		Border border1 = BorderFactory.createTitledBorder(" Server 1 ");
//		
//		server1.setBorder(border1);
//		
//		JTextArea server2 = new JTextArea();
//		server2.setEditable(false);
//		Border border2 = BorderFactory.createTitledBorder(" Server 2 ");
//		server2.setBorder(border2);
//		
//		server.setBackground(Color.WHITE);
//		server.add(server1);
//		server.add(server2);
//		return server;
//	}

	public void update() {
		StringBuilder customerQueue = queue.getQueue();
		queue1.setText(customerQueue.toString());
		
	}
	
}
