package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {
    abstract BigDecimal getPrice();

    // For pretty details display in UI
    abstract String detailsString();
}
