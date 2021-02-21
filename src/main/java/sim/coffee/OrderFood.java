package sim.coffee;

import java.math.BigDecimal;

public class OrderFood extends OrderItem {
    @Override
    public BigDecimal getPrice() {
        return new BigDecimal("0");
    }

    @Override
    String detailsString() {
        return "";
    }
}
