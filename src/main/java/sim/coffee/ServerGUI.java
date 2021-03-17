package sim.coffee;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

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
		StringBuilder order = server.getCurrentOrder();
		setText(order.toString());
	}	
}
