package sim.app;

public class Order {

    private String customerId;
    private String item;

    public Order(String customerId, String item) {
        this.customerId = customerId;
        this.item = item;
    }

    public String getCustomerID() {
        return customerId;
    }

    public String getItemId() {
        return item;
    }
}
