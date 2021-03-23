package sim.view;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.app.Order;
import sim.model.Server;

public class ServerGUI extends JTextArea implements Observer {
	private Server server;
	private int serverNr;

	public ServerGUI(Server server, int serverNr) {
		this.server = server;
		this.serverNr = serverNr;
		server.registerObserver(this);

		setup();
	}

	public void setup() {
		setEditable(false);
		Border border1 = BorderFactory.createTitledBorder("Server " + serverNr);
		setBorder(border1);
	}

	public void update() {

		Order[] order = server.getCurrentOrder();

		if (order == null) {
			setText("");
		} else {
			StringBuilder currentOrder = new StringBuilder();
			currentOrder.append("Customer being served: \n");
			currentOrder.append(String.format("%10s", order[0].getCustomerID()) + "\n");
			currentOrder.append("Ordered items: \n");
			for (Order o : order) {
				currentOrder.append(String.format("%10s", o.getItemId()) + "\n");
			}

			setText(currentOrder.toString());
		}
	}
}
