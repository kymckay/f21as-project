package sim.coffee;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class KitchenQueueGUI extends JTextArea implements Observer{
	
	SharedQueue kitchenQueue;
	
	public KitchenQueueGUI(SharedQueue kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
		kitchenQueue.registerObserver(this);
		setup();	
	}
	
	public void setup() {
		setEditable(false);
		Border border = BorderFactory.createTitledBorder("Kitchen Queue");
		setBorder(border);
	}

	@Override
	public void update() {
		StringBuilder kitchenQueueCurrent = kitchenQueue.getQueue();
		setText(kitchenQueueCurrent.toString());		
	}

}
