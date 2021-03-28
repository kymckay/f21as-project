package sim.view;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.app.Order;
import sim.model.Kitchen;

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
		Order[] order = kitchen.getCurrentOrder();

		if (order == null) {
			setText("");
		} else {
			StringBuilder currentOrder = new StringBuilder();
			currentOrder.replace(0, currentOrder.length(), "");
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
