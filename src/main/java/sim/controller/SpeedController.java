package sim.controller;

import java.util.Timer;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sim.model.Server;
import sim.view.ServerGUI;

public class SpeedController {
	
	private Server server;
	private ServerGUI gui;
	private Long baseSpeed;
	private Long newSpeed;
	
	public SpeedController(Server server, ServerGUI gui) {
		this.server = server;
		this.gui = gui;
		baseSpeed = server.getBaseSpeed();
		
		gui.addSetListener(new SliderListener());
	}
	
	public class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			if (!source.getValueIsAdjusting()) { 
				int speed = (int)source.getValue();
				if (speed < 0) {
					newSpeed = baseSpeed / (speed * -1);
					server.setSpeed(newSpeed);
				} 
				else if (speed > 0) {
					newSpeed = baseSpeed * (speed);
					server.setSpeed(newSpeed);
				}
				else {
					server.setSpeed(baseSpeed);	
				}
			}
		}
		
	}

}
