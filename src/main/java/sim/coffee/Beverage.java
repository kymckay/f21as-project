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

	@Override
	public Size[] getSizes() {
		return sizes;
	}

	@Override
	public boolean canBeHot() {
		return canBeHot;
	}

	@Override
	public Milk[] getMilks() {
		return milks;
	}

	// Chosen size and milk changes the price of this item
	public BigDecimal getPrice(Size s, Milk m) {
		BigDecimal withMilk = getPrice().add(BigDecimal.valueOf(m.getAddOnPrice()));

		return withMilk.multiply(BigDecimal.valueOf(s.getCoefficient()));
	}
}
