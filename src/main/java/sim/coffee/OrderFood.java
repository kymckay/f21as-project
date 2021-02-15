package sim.coffee;

import java.math.BigDecimal;

public class OrderFood extends OrderItem {

    public OrderFood(MenuItem m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    @Override
    public BigDecimal getPrice() {
        // TODO generate additional cost feature & getBasePrice()
        return null;
        // return menuItem.getBasePrice();
    }
    
}
