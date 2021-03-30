package sim.controllers;

import java.awt.event.*;

import javax.swing.text.View;

import sim.model.CoffeeShop;
import sim.views.SimulationGUI;

public class ServerController {
    private CoffeeShop model;
    private SimulationGUI view;

    public ServerController(CoffeeShop model, SimulationGUI view) {
        this.model = model;
        this.view = view;

        view.addAddServerListener(new AddListener());
        view.addRemoveServerListener(new RemoveListener());
    }

    public class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int initialNum = model.getNumStaff();
            model.setNumStaff(initialNum + 1);
            model.updateServer();
            view.updateServer("add");
        }
    }

    public class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int initialNum = model.getNumStaff();
            model.setNumStaff(initialNum - 1);
            model.updateServer();
            view.updateServer("Remove");
        }
    }
}