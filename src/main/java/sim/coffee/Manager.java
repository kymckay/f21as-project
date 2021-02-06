package sim.coffee;

public class Manager {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.readFile("data/menu.csv");

        OrderList orders = new OrderList();
        orders.readFile("data/orders.csv");

        new CustomerGUI(
            new MenuTableModel(menu),
            orders,
            new OrderTableModel()
        );
    }
}