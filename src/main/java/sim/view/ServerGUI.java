package sim.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sim.app.Order;
import sim.model.Server;

public class ServerGUI extends JPanel implements Observer {
	// Static variable tracks number of servers that exist
	private static int count = 0;

	private Server server;
	private int number;
	
	private JTextArea serverArea;
	private JSlider serverSlider;
	
	static final int MIN = -10;
	static final int MAX = 10;
	static final int INIT = 0;

	public ServerGUI(Server server) {
		this.server = server;
		this.number = ++count;
		server.registerObserver(this);
		setLayout(new GridLayout(2,1));
		add(setupControls());

		setup();
	}

	public void setup() {
		
		serverArea = new JTextArea();
		serverArea.setEditable(false);
		Border border1 = BorderFactory.createTitledBorder("Server " + number);
		JScrollPane serverPane = new JScrollPane(serverArea);
		serverPane.setBorder(border1);
		add(serverPane);
	}
	
	public JSlider setupControls() {
		serverSlider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INIT);
		
		serverSlider.setMajorTickSpacing(5);
		serverSlider.setPaintTicks(true);
		serverSlider.setPaintLabels(true);
		
		//Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( MIN, new JLabel("Slow") );
		labelTable.put( MAX, new JLabel("Fast") );
		serverSlider.setLabelTable( labelTable );
		
		return serverSlider;
	}
	
	// add listener to update button
	public void addSetListener(ChangeListener ce) {
		serverSlider.addChangeListener(ce);
	}

	public void update() {

		Order[] order = server.getCurrentOrder();

		if (order == null) {
			serverArea.setText("");
		} else {
			StringBuilder currentOrder = new StringBuilder();
			currentOrder.append("Customer being served: \n");
			currentOrder.append(String.format("%10s", order[0].getCustomerID()) + "\n");
			currentOrder.append("Ordered items: \n");
			for (Order o : order) {
				currentOrder.append(String.format("%10s", o.getItemId()) + "\n");
			}

			serverArea.setText(currentOrder.toString());
		}
	}
}
