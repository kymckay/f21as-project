package sim.controllers;

import java.util.Timer;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sim.model.Server;
import sim.views.ServerGUI;

public class SpeedController {

	private Server server;
	private ServerGUI gui;
	private Long baseSpeed;
	private Long newSpeed;

	public SpeedController(Server server, ServerGUI gui) {
		this.server = server;
		this.gui = gui;
		baseSpeed = server.getBaseSpeed();
		// register SliderListener with the gui
		gui.addSetListener(new SliderListener());
	}

	// inner SliderListener class responds when user selects a new simulation speed
	public class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			if (!source.getValueIsAdjusting()) {
				int speed = (int)source.getValue(); // gets speed selected on the slider
				if (speed < 0) { // speed has been decreased
					newSpeed = baseSpeed * (speed * -1);
					server.setSpeed(newSpeed);
				}
				else if (speed > 0) { // speed has been increased
					newSpeed = baseSpeed / (speed);
					server.setSpeed(newSpeed);
				}
				else { // speed selected to normal
					server.setSpeed(baseSpeed);
				}
			}
		}
	}
}
