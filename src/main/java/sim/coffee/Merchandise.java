package sim.coffee;

import java.math.BigDecimal;

public class Merchandise extends MenuItem {
	
	private Label[]  labels;
    private Colour[] colours;
    private String   id;
    
    public Merchandise(Label[] labels, Colour[] colours, String id, BigDecimal price, String description) 
    															               throws IllegalIDException {
    	super(price, description);

    	this.labels  = labels;
    	this.colours = colours;
    	
    	boolean idMatches = id.matches("^[M]\\d{3}"); //Beverage ID has to match the following regex pattern
		if (idMatches) {
			this.id      = id;
		} else {
			throw new IllegalIDException(id);
		}
    }
    
    public Label[] getLabels() {
    	return labels;
    }
    
    public Colour[] getColours() {
    	return colours;
    }
    
	public String getID() {
		return id;
	}

}
