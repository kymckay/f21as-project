package sim.coffee;

import java.math.BigDecimal;

public class Food extends MenuItem {
	
	//instance variable
	private DietaryClass[] dietaryClasses;
	
	//constructor
	public Food(DietaryClass[] dietaryClasses, String id, BigDecimal price, String description)
														             throws IllegalIDException {
		
		super(id, price, description);
		this.dietaryClasses = dietaryClasses;
	}
	
	//getter method
	public DietaryClass[] getDietaryClass() {
		return dietaryClasses;
	}
    
}
