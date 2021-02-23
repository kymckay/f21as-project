package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class MenuItem {

	//instance variables
	private String     id;
	private BigDecimal price;
	private String     description;
	private int		   orderCount;

	//constructor
	MenuItem (String categoryID, String id, BigDecimal price, String description, int count) throws IllegalIDException {
    	boolean idMatches 		= id.matches("^[BFM]\\d{3}"); // ID must match the following regex pattern
    	boolean categoryMatches = id.startsWith(categoryID); // ID must start with the correct category ID, defined in each subclass

    	if (!(idMatches && categoryMatches)) {
			throw new IllegalIDException(id);
		}

		this.id = id;
		this.price = price;
		this.description = description;
		orderCount = count;
	}

	//getter methods
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

}
