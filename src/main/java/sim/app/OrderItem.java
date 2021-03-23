package sim.app;

import java.math.BigDecimal;

public class OrderItem {

    private String itemId;
    private String itemDetails;
    private BigDecimal fullPrice;
    private BigDecimal pricePaid;

    public OrderItem(String itemId, String itemDetails, BigDecimal fullPrice, BigDecimal pricePaid) {
        this.itemId = itemId;
        this.itemDetails = itemDetails;
        this.fullPrice = fullPrice;
        this.pricePaid = pricePaid;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }
}
