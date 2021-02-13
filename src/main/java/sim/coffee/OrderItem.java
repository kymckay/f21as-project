package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {

    private MenuItem menuItem;

    public OrderItem(MenuItem m) {
        menuItem = m;
    }
    
    abstract BigDecimal getPrice();

}
