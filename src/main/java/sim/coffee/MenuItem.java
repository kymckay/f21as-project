package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract public class MenuItem {
	
	//instance variables 
	private BigDecimal price;
	private String     description;
    
	//constructor
	public MenuItem (BigDecimal price, String description) 
												throws IllegalIDException {
		
		
		this.price       = price;
		this.description = description;
	}
	
	//getter methods
	public BigDecimal getPrice() {
		return price.setScale(2, RoundingMode.DOWN);
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract String getID();
}
