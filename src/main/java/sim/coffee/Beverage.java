package sim.coffee;

import java.math.BigDecimal;

public class Beverage extends MenuItem {
	
	//instance variables
	private Size[]  sizes;
	private boolean canBeHot;
	private Milk[]  milks;
	
	//constructor
	public Beverage(Size[] sizes, boolean canBeHot, Milk[] milks, 
					String id, BigDecimal price, String description) throws IllegalIDException{
		
		super(id, price, description);
		this.sizes    = sizes;
		this.canBeHot = canBeHot;
		this.milks    = milks;
	
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
	
}
