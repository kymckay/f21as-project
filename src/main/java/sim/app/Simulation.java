package sim.app;

import sim.controllers.ServerController;
import sim.controllers.SpeedController;
import sim.model.CoffeeShop;
import sim.model.Menu;
import sim.views.ServerGUI;
import sim.views.SimulationGUI;

public class Simulation {
    public static void main(String[] args) {
        Menu menu = new Menu("data/menu.csv");
        CoffeeShop model = new CoffeeShop(menu);

        SimulationGUI view = new SimulationGUI(model);

        // Register the server speed controllers
        for (ServerGUI gui : view.getStaffViews()) {
            new SpeedController(gui.getModel(), gui);
        }
    }
}