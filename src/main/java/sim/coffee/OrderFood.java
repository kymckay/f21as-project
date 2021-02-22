package sim.coffee;

import java.math.BigDecimal;

public class OrderFood extends OrderItem {
    
    OrderFood(String id) {
        super(id);
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal("0");
    }

    @Override
    String detailsString() {
        return "";
    }
}
