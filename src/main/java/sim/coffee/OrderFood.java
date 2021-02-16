package sim.coffee;

import java.math.BigDecimal;

public class OrderFood extends OrderItem {

    public OrderFood(MenuItem m) {
        super(m);
    }

    @Override
    public BigDecimal getPrice() {
        return menuItem.getPrice();
    }
    
}
