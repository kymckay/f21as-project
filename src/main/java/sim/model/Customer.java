package sim.model;

public class Customer {

    String name;
    MenuItem[] order;

    public Customer(String name, MenuItem[] order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public MenuItem[] getOrder() {
        return order;
    }
}
