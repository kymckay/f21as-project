package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class MenuItem {

	//instance variables
	private String id;
	private BigDecimal price;
	private String description;

	// Nothing is ordered by default
	private int orderCount = 0;

	//constructor
	MenuItem (String categoryID, String id, BigDecimal price, String description) throws IllegalIDException {
    	boolean idMatches 		= id.matches("^[BFM]\\d{3}"); // ID must match the following regex pattern
    	boolean categoryMatches = id.startsWith(categoryID); // ID must start with the correct category ID, defined in each subclass

    	if (!(idMatches && categoryMatches)) {
			throw new IllegalIDException(id);
		}

		this.id = id;
		this.price = price;
		this.description = description;
	}

	public String getID() {
		return id;
	}

	public BigDecimal getPrice() {
		return price.setScale(2, RoundingMode.DOWN);
	}

	public String getDescription() {
		return description;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setCount() {
		orderCount++;
	}

    // The UI needs to be able to tell certain properties about items
    // These default values are overridden where apropriate in the subclasses
	// Note there is probably a better design pattern for this, it feels hacky
	// Works for now though
    public boolean canBeHot() {
        return false;
	}
	public Size[] getSizes() {
		return new Size[0];
	}
	public Milk[] getMilks() {
		return new Milk[0];
	}
	public Colour[] getColours() {
		return new Colour[0];
	}
	public Label[] getLabels() {
		return new Label[0];
	}
}
