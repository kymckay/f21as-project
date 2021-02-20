package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {

    protected MenuItem menuItem;

    public OrderItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
    
    abstract BigDecimal getPrice();

}
