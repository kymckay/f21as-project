package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract public class MenuItem {
	
	//instance variables 
	private String 	   categoryID;
	private String 	   id;
	private BigDecimal price;
	private String     description;
    
	//constructor
	public MenuItem (String categoryID, String id, BigDecimal price, String description) 
												throws IllegalIDException {
		
    	this.categoryID    = categoryID;
		
    	boolean idMatches 		= id.matches("^[BFM]\\d{3}"); // ID must match the following regex pattern
    	boolean categoryMatches = id.startsWith(categoryID); // ID must start with the correct category ID, defined in each subclass
    	
    	if (idMatches && categoryMatches) {
			this.id      = id;
		} else {
			throw new IllegalIDException(id);
		}
		
		this.price       = price;
		this.description = description;
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
	
}	
