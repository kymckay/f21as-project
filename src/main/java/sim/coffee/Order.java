package sim.coffee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {

    private LocalDateTime time;
    private String customerId;
    private OrderItem item;

    public Order(LocalDateTime t, String customerId, OrderItem item) {
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

        return new BigDecimal("1").subtract(finalPrice.divide(menuPrice));
    }
}
