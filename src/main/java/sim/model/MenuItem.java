package sim.model;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MenuItem {

    //instance variables
    private String id;
    private BigDecimal price;
    private String name;

    // Nothing is ordered by default
    private int orderCount = 0;

    MenuItem (String id, BigDecimal price, String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public BigDecimal getPrice() {
        return roundPrice(price);
    }

    // Convenient way to ensure price outputs are all same rounding and precision
    protected BigDecimal roundPrice(BigDecimal price) {
        // Prices should always be to 2 decimal places (no fractional pennies)
        // Half even typically rounding method for finance
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public String getName() {
        return name;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setCount() {
        orderCount++;
    }
}
