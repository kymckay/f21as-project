package sim.coffee;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class KitchenGUI extends JTextArea implements Observer {
	private Kitchen kitchen;
	
	public KitchenGUI(Kitchen kitchen) {
		this.kitchen = kitchen;
		kitchen.registerObserver(this);
		
		setup();
	}
	
	public void setup() {
		setEditable(false);
		Border border = BorderFactory.createTitledBorder("Kitchen");
		setBorder(border);
	}
	
	@Override
	public void update() {
		StringBuilder order = kitchen.getCurrentOrder();
		setText(order.toString());
	}
}
