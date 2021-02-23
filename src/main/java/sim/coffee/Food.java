package sim.coffee;

import java.math.BigDecimal;

public class Food extends MenuItem {
	
	//instance variable
	private DietaryClass[] dietaryClasses;
	private final static String categoryID = "F";
	
	
	//constructor
	public Food(DietaryClass[] dietaryClasses, String id, BigDecimal price, String description, int count)
														             throws IllegalIDException {
		
		super(categoryID,id, price, description, count);
		this.dietaryClasses = dietaryClasses;
	
	}
	
	//getter method
	public DietaryClass[] getDietaryClass() {
		return dietaryClasses;
	}
}
