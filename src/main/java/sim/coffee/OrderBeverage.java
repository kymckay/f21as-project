package sim.coffee;

import java.math.BigDecimal;

public class OrderBeverage extends OrderItem {

    private Size size;
    private boolean isHot;
    private Milk milk;

    public OrderBeverage(String id, Size s, boolean hot, Milk milk) {
        super(id);
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
        BigDecimal result = new BigDecimal("0");
        // Get base price and add result
        result = result.add(new BigDecimal("0"));
        // Add the addOn price of milk to result
        result = result.add(BigDecimal.valueOf(milk.getAddOnPrice()));
        // multiply result by a multiplier for its respectiver sizes
        result = result.multiply(BigDecimal.valueOf(size.getCoefficient()));
        return result;
    }

    @Override
    String detailsString() {
        return String.format("%s|%b|%s", size, isHot, milk);
    }

}