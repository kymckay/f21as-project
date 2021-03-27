package sim.app;

import sim.model.CoffeeShop;
import sim.views.SimulationGUI;

public class Manager {
    public static void main(String[] args) {
        new SimulationGUI(new CoffeeShop(3));
    }
}