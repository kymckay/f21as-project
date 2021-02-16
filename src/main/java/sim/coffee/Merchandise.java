package sim.coffee;

import java.math.BigDecimal;

public class Merchandise extends MenuItem {
	
    private Label[]  labels;
    private Colour[] colours;
    
    public Merchandise(Label[] labels, Colour[] colours, String id, BigDecimal price, String description) 
    															               throws IllegalIDException {
    	
    	super(id, price, description);
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
