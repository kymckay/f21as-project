package sim.controllers;

import java.awt.event.*;

import sim.model.CoffeeShop;
import sim.views.SimulationGUI;

public class ServerController {
    private CoffeeShop model;

    public ServerController(CoffeeShop model, SimulationGUI view) {
        this.model = model;

        view.addAddServerListener(new AddListener());
        view.addRemoveServerListener(new RemoveListener());
    }

    public class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int initialNum = model.getNumStaff();
            model.setNumStaff(initialNum++);
        }
    }

    public class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int initialNum = model.getNumStaff();
            model.setNumStaff(initialNum--);
        }
    }
}