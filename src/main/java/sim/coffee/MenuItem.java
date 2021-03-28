package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class MenuItem {

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
		return roundPrice(price);
	}

	// Convenient way to ensure price outputs are all same rounding and precision
	protected BigDecimal roundPrice(BigDecimal price) {
		// Prices should always be to 2 decimal places (no fractional pennies)
		// Half even typically rounding method for finance
		return price.setScale(2, RoundingMode.HALF_EVEN);
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
}
