package sim.coffee;

import java.math.BigDecimal;

public class OrderItem {

    private String itemId;
    private String itemDetail;
    private BigDecimal fullPrice;
    private BigDecimal pricePaid;

    public OrderItem(String itemId, String itemDetail, BigDecimal fullPrice, BigDecimal pricePaid) {
        this.itemId = itemId;
        this.itemDetail = itemDetail;
        this.fullPrice = fullPrice;
        this.pricePaid = pricePaid;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemDetail() {
        return itemDetail;
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
