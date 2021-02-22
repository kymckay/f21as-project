package sim.coffee;

import java.math.BigDecimal;

public class OrderMerchandise extends OrderItem {

    private Label label;
    private Colour colour;

    public OrderMerchandise(String id, Label lab, Colour col) {
        super(id);
        label = lab;
        colour = col;
    }

    public Label getLabel() {
        return label;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal result = new BigDecimal(0);
        result = result.add(new BigDecimal("0"));
        result = result.add(BigDecimal.valueOf(label.getAddOnPrice()));
        return result;
    }

    @Override
    String detailsString() {
        return String.format("%s|%s", label, colour);
    }
}
