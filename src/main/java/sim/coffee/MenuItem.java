package sim.coffee;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract public class MenuItem {
    
	//instance variables 
	private String     id;
	private BigDecimal price;
	private String     description;
	
	//constructor
	public MenuItem (String id, BigDecimal price, String description) {
		
		this.id          = id;
		this.price       = price; 
		this.description = description;
		
	}
	
	//getter methods
	public String getID() {
		return id;
	}
	
	public BigDecimal getPrice() {
		return price.setScale(2, RoundingMode.DOWN); //rounds the number to two decimal places
	}
	
	public String getDescription() {
		return description;
	}
	
	//overrides toStirng() method
	//figured it would make sense for each subclass to have its own toString() given the additional info (e.g. size for beverages)
	public abstract String toString(); 
	
 	
}
