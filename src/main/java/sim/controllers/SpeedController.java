package sim.controllers;

import static sim.model.Server.BASE_SPEED;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sim.model.Server;
import sim.views.ServerGUI;

public class SpeedController {
	private Server model;
	private ServerGUI view;

	public SpeedController(Server model, ServerGUI view) {
		this.model = model;
		this.view = view;

		// register SliderListener with the gui
		view.addSetListener(new SliderListener());
	}

	// inner SliderListener class responds when user selects a new simulation speed
	public class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			// gets speed selected on the slider
			int speed = view.getSpeed();

			if (speed < 0) {
				// speed has been decreased
				model.setSpeed(BASE_SPEED * (speed * -1));
			} else if (speed > 0) {
				// speed has been increased
				model.setSpeed(BASE_SPEED / (speed));
			} else {
				// speed set to normal
				model.setSpeed(BASE_SPEED);
			}
		}
	}
}
