package sim.coffee;

import java.math.BigDecimal;

public class Food extends MenuItem {

	private DietaryClass[] dietaryClasses;
	private static final String CAT_ID = "F";

	public Food(DietaryClass[] dietaryClasses, String id, BigDecimal price, String description)
			throws IllegalIDException {

		super(CAT_ID,id, price, description);
		this.dietaryClasses = dietaryClasses;

	}

	public DietaryClass[] getDietaryClass() {
		return dietaryClasses;
	}
}
