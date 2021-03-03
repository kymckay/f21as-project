package sim.coffee;

import java.math.BigDecimal;

public class Beverage extends MenuItem {

	private Size[]  sizes;
	private boolean canBeHot;
	private Milk[]  milks;
	private static final String CAT_ID = "B";

	// Ensures details across beverage orders are consistently formatted
	public static String formatDetails(Size s, boolean hot, Milk m) {
		return String.format("%s|%b|%s", s, hot, m);
	}

	public Beverage(Size[] sizes, boolean canBeHot, Milk[] milks, String id, BigDecimal price, String description)
			throws IllegalIDException {

		super(CAT_ID, id, price, description);

		this.sizes    = sizes;
		this.canBeHot = canBeHot;
		this.milks    = milks;
	}

	public Size[] getSizes() {
		return sizes;
	}

	public boolean canBeHot() {
		return canBeHot;
	}

	public Milk[] getMilks() {
		return milks;
	}

	// Chosen size and milk changes the price of this item
	public BigDecimal getPrice(Size s, Milk m) {
		// Smallest possible size recieves base price, scale coefficients accordingly
		// (i.e. wouldn't make sense to multiply if only "large" is available)
		// note: sizes must always be listed smallest to largest in data
		double scaledCoef = s.getCoefficient() / sizes[0].getCoefficient();

		// Milk adds to the price
		BigDecimal withMilk = getPrice().add(BigDecimal.valueOf(m.getAddOnPrice()));

		// Size increases quantity of milk too as a coefficient
		BigDecimal price = withMilk.multiply(BigDecimal.valueOf(scaledCoef));

		return roundPrice(price);
	}
}
