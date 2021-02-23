package sim.coffee;

import java.math.BigDecimal;

public class Merchandise extends MenuItem {
	
	private Label[]  labels;
    private Colour[] colours;
    private final static String categoryID = "M";
    
    
    public Merchandise(Label[] labels, Colour[] colours, String id, BigDecimal price, String description, int count) 
    															               throws IllegalIDException {
    	super(categoryID, id, price, description, count);

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
