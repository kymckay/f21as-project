package sim.views;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.interfaces.Observer;
import sim.model.Customer;
import sim.model.Kitchen;
import sim.model.MenuItem;

public class KitchenGUI extends JTextArea implements Observer {
	private Kitchen kitchen;

	public KitchenGUI(Kitchen kitchen) {
		this.kitchen = kitchen;
		kitchen.registerObserver(this);

		setup();
	}

	public void setup() {
		setEditable(false);
		setText("No orders being prepared.");
		setFont(new Font("Monospaced", Font.PLAIN, 13));
		Border border = BorderFactory.createTitledBorder("Kitchen");
		setBorder(border);
	}

	@Override
	public void update() {
		Customer customer = kitchen.getCurrentCustomer();

		if (customer == null) {
			setText("");
		} else {
			StringBuilder currentOrder = new StringBuilder();
			currentOrder.replace(0, currentOrder.length(), "");
			currentOrder.append("Customer being served: \n");
			currentOrder.append("   " + customer.getName() + "\n");
    		currentOrder.append("Ordered items: \n");
    		for (MenuItem o : customer.getOrder()) {
    			currentOrder.append("   " + o.getID() + "\n");
    		}
    		setText(currentOrder.toString());
		}
	}
}
