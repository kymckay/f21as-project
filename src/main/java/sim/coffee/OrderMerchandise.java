package sim.coffee;

import java.math.BigDecimal;

public class OrderMerchandise extends OrderItem {

    private Label label;
    private Colour colour;

    public OrderMerchandise(MenuItem m, Label lab, Colour col) {
        super(m);
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
        // TODO generate additional cost feature
        return menuItem.getBasePrice();
    }
    
}
