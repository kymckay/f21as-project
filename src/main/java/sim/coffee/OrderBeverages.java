package sim.coffee;

import java.math.BigDecimal;

public class OrderBeverages extends OrderItem {

    private Size size;
    private boolean isHot;
    private Milk milk;

    public OrderBeverages(Size s, boolean hot, Milk milk, MenuItem m) {
        super(m);
        size = s;
        isHot = hot;
        this.milk = milk;
    }

    public Size getSize() {
        return size;
    }

    public boolean isHot() {
        return isHot;
    }

    public Milk getMilk() {
        return milk;
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal result = new BigDecimal(0);
        // Get base price and add result
        result = result.add(menuItem.getPrice());
        // Add the addOn price of milk to result
        result = result.add(BigDecimal.valueOf(milk.getAddOnPrice()));
        // multiply result by a multiplier for its respectiver sizes
        result = result.multiply(BigDecimal.valueOf(size.getCoefficient()));
        return result;
    }
    
}