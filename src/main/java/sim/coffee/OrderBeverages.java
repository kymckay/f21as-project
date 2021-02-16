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
        // TODO generate additional cost feature & getBasePrice()
        return null;
        // return menuItem.getBasePrice();
    }
    
}