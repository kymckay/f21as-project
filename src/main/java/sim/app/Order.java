package sim.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {

    private LocalDateTime time;
    private String customerId;
    private OrderItem item;

    public Order(LocalDateTime t, String customerId, OrderItem item, String) {
        time = t;
        this.customerId = customerId;
        this.item = item;
    }

    public LocalDateTime getTime() {
        return time;
    }

    // added getDate() to get date without time.
    public LocalDate getDate() {
        return time.toLocalDate();
    }

    public String getCustomerID() {
        return customerId;
    }

    public String getItemDetails() {
        return item.getItemDetails();
    }

    public BigDecimal getFullPrice() {
        return item.getFullPrice();
    }

    public BigDecimal getPricePaid() {
        return item.getPricePaid();
    }

    public String getItemId() {
        return item.getItemId();
    }

    // Used to apply discounts in the basket
    public void setPricePaid(BigDecimal newPrice) {
        item.setPricePaid(newPrice);
    }

    public boolean hasDiscount() {
        // Compare to ignores scale, just compares value
        // returns -1, since getPricePaid() > getFullPrice
        return getPricePaid().compareTo(getFullPrice()) < 0;
    }

    // Calculates the percentage discount (between 0 and 1)
    public BigDecimal getDiscount() {
        BigDecimal menuPrice = getFullPrice();
        BigDecimal finalPrice = getPricePaid();

        // Percentage of total, rounded to avoid repeating decimals (half up for standard rounding)
        BigDecimal percentage = finalPrice.divide(menuPrice, 2, RoundingMode.HALF_UP);

        // Want to return percentage off total
        return new BigDecimal("1").subtract(percentage);
    }
}
