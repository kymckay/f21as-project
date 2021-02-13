package sim.coffee;

import java.math.BigDecimal;

public abstract class OrderItem {

    private MenuItem menuItem;

    public OrderItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
    
    abstract BigDecimal getPrice();

}
