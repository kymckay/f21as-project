package sim.coffee;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {


    // Instance variables
    private LocalDateTime time;
    public String customerId;
    private OrderItem itemDetails;
    private BigDecimal pricePaid;

    public Order(LocalDateTime t, String id, OrderItem deets, BigDecimal price) {
        time = t;
        this.customerId = id;
        itemDetails = deets;
        pricePaid = price;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getCustomerID() {
        return customerId;
    }

    public OrderItem getItemDetails() {
        return itemDetails;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }


}
