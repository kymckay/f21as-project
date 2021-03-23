package sim.app;

import sim.view.SimulationGUI;
import sim.model.CoffeeShop;

public class Manager {
    public static void main(String[] args) {
        new SimulationGUI(new CoffeeShop(3));
    }
}