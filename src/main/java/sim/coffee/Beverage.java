package sim.coffee;

import java.math.BigDecimal;

public class Beverage extends MenuItem {

	private Size[]  sizes;
	private boolean canBeHot;
	private Milk[]  milks;
	private static final String CAT_ID = "B";

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

	@Override
	public boolean canBeHot() {
		return canBeHot;
	}

	public Milk[] getMilks() {
		return milks;
	}
}
