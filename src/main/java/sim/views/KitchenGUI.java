package sim.views;

import java.util.Optional;

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
		Border border = BorderFactory.createTitledBorder("Kitchen");
		setBorder(border);
	}

	@Override
	public void update() {
		Optional<Customer> customer = kitchen.getCurrentCustomer();

		if (customer.isPresent()) {
			Customer serving = customer.get();

			StringBuilder currentOrder = new StringBuilder();

			currentOrder.replace(0, currentOrder.length(), "");
			currentOrder.append("Customer being served: \n");
			currentOrder.append(String.format("%10s", serving.getName()) + "\n");
			currentOrder.append("Ordered items: \n");
			for (MenuItem o : serving.getOrder()) {
				currentOrder.append(String.format("%10s", o.getID()) + "\n");
			}
			setText(currentOrder.toString());
		} else {
			setText("");
		}
	}
}
