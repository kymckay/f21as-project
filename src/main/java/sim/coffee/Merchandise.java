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

    public Label[] getLabels() {
    	return labels;
    }

    public Colour[] getColours() {
    	return colours;
    }
}
