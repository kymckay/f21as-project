package sim.coffee;

import java.math.BigDecimal;

public class Merchandise extends MenuItem {

	private Label[]  labels;
    private Colour[] colours;
    private static final String CAT_ID = "M";

    public Merchandise(Label[] labels, Colour[] colours, String id, BigDecimal price, String description)
            throws IllegalIDException {

    	super(CAT_ID, id, price, description);

    	this.labels  = labels;
    	this.colours = colours;
    }

    @Override
    public Label[] getLabels() {
    	return labels;
    }

    @Override
    public Colour[] getColours() {
    	return colours;
    }

    // A chosen label influences the price of this item
    public BigDecimal getPrice(Label l) {
        return getPrice().add(BigDecimal.valueOf(l.getAddOnPrice()));
    }
}
