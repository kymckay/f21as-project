package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {

    protected MenuItem menuItem;

    public OrderItem(MenuItem m) {
        menuItem = m;
    }
    
    abstract BigDecimal getPrice();

}