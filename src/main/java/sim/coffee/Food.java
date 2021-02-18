package sim.coffee;

import java.math.BigDecimal;

public class Food extends MenuItem {
	
	//instance variable
	private DietaryClass[] dietaryClasses;
	private String         id;
	
	//constructor
	public Food(DietaryClass[] dietaryClasses, String id, BigDecimal price, String description)
														             throws IllegalIDException {
		
		super(price, description);
		this.dietaryClasses = dietaryClasses;
		
		boolean idMatches = id.matches("^[F]\\d{3}"); //Beverage ID has to match the following regex pattern
		if (idMatches) {
			this.id       = id;
		} else {
			throw new IllegalIDException(id);
		}	
	}
	
	//getter method
	public DietaryClass[] getDietaryClass() {
		return dietaryClasses;
	}
	
	public String getID() {
		return id;
	}
    
}
