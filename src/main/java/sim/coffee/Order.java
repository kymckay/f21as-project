package sim.coffee;

import java.math.BigDecimal;
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

    public String getCustomerID() {
        return customerId;
    }

    public String getItemDetail() {
        return item.getItemDetail();
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
}
