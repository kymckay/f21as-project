package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract public class MenuItem {
	
	//instance variables 
	private String     id;
	private BigDecimal price;
	private String     description;
    
	//constructor
	public MenuItem (String id, BigDecimal price, String description) 
												throws IllegalIDException {
		
		boolean idMatches = id.matches("^[FMB]\\d{3}");
		if (idMatches) {
			this.id          = id;
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
