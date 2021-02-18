package sim.coffee;

import java.math.BigDecimal;

public class Beverage extends MenuItem {
	
	//instance variables
	
	private Size[]  sizes;
	private boolean canBeHot;
	private Milk[]  milks;
	private String  id;
	
	//constructor
	public Beverage(Size[] sizes, boolean canBeHot, Milk[] milks, 
					String id, BigDecimal price, String description) throws IllegalIDException{
		
		super(price, description);
		
		this.sizes    = sizes;
		this.canBeHot = canBeHot;
		this.milks    = milks;
		
		boolean idMatches = id.matches("^[B]\\d{3}"); //Beverage ID has to match the following regex pattern
		
		if (idMatches) {
			this.id      = id;
		} else {
			throw new IllegalIDException(id);
		}
	
	}
	//getter methods
	public Size[] getSizes() {
		return sizes;
	}
	
	public boolean canBeHot() {
		return canBeHot;
	}
	
	public Milk[] getMilks() {
		return milks;
	}
	
	public String getID() {
		return id;
	}
	
}
