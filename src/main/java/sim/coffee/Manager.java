package sim.coffee;

import java.io.FileNotFoundException;

public class Manager {
    public static void main(String[] args) {
        Menu menu;
        OrderList orders;

        // If input menu or orders file isn't found program cannot continue
        try {
            menu = new Menu("data/menu.csv");
            orders = new OrderList("data/orders.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return; // This just placates the linter
        }

        new CustomerGUI(
            new MenuTableModel(menu),
            orders,
            new OrderTableModel()
        );
    }
}