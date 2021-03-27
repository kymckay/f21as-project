package sim.app;

import sim.controllers.SpeedController;
import sim.model.CoffeeShop;
import sim.views.ServerGUI;
import sim.views.SimulationGUI;

public class Simulation {
    public static void main(String[] args) {
        CoffeeShop model = new CoffeeShop(3);

        SimulationGUI view = new SimulationGUI(model);

        // Register the server speed controllers
        for (ServerGUI gui : view.getStaffViews()) {
            new SpeedController(gui.getModel(), gui);
        }
    }
}