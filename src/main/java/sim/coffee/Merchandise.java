package sim.coffee;

import java.math.BigDecimal;

public class Merchandise extends MenuItem {

	private Label[]  labels;
    private Colour[] colours;
    private static final String CAT_ID = "M";

    // Ensures details across merch orders are consistently formatted
    public static String formatDetails(Label l, Colour c) {
        return String.format("%s|%s", l, c);
    }

    public Merchandise(Label[] labels, Colour[] colours, String id, BigDecimal price, String description)
            throws IllegalIDException {

    	super(CAT_ID, id, price, description);

    	this.labels  = labels;
    	this.colours = colours;
    }

    public Label[] getLabels() {
    	return labels;
    }

    public Colour[] getColours() {
    	return colours;
    }

    // A chosen label influences the price of this item
    public BigDecimal getPrice(Label l) {
        BigDecimal price = getPrice().add(BigDecimal.valueOf(l.getAddOnPrice()));

        return roundPrice(price);
    }
}
