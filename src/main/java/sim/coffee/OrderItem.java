package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {

    private String id;

    OrderItem (String id) {
        id = this.id;
    }

    public String getId() {
        return id;
    }

    abstract BigDecimal getPrice();

    // For pretty details display in UI
    abstract String detailsString();
}
