package sim.coffee;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Manager {
    public static void main(String[] args) {
        Menu menu;
        OrderList orders;

        // If input menu or orders file isn't found program cannot continue
        try {
            menu = new Menu("data/menu.csv");

            // List of placed orders uses a linked list since it's most often being appended
            // Could become quite long if a large quantity of orders are processed
            orders = new OrderList("data/orders.csv", new LinkedList<>());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return; // This just placates the linter
        }

        // Basket uses an array list since it's more often being accessed by the table display
        // Additonally basket is likely never going to contain a significantly long list
        OrderBasket basket = new OrderBasket(menu, orders, new ArrayList<>());

        new CustomerGUI(
            new MenuTableModel(menu),
            new BasketTableModel(basket)
        );
    }
}