package sim.coffee;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {


    // Instance variables
    private LocalDateTime time;
    private String customerId;
    private String itemId;
    private String itemDetail;
    private BigDecimal fullPrice;
    private BigDecimal pricePaid;

    public Order(LocalDateTime t, String customerId, String itemId, String itemDetail, BigDecimal fullPrice, BigDecimal pricePaid) {
        time = t;
        this.customerId = customerId;
        this.itemId = itemId;
        this.itemDetail = itemDetail;
        this.fullPrice = fullPrice;
        this.pricePaid = pricePaid;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getCustomerID() {
        return customerId;
    }

    public String getItemDetails() {
        return itemDetail;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    // Used to apply discounts
    public boolean setPricePaid(BigDeicmal newPrice) {
        this.pricePaid = newPrice;
    }
}
