package sim.coffee;
import java.math.BigDecimal;

public class Beverage extends MenuItem {
	
	//instance variables
	public  enum    Size {S, M, L};
	private Size    size;
	private boolean hot;
	public  enum    Milk {NONE, REGULAR, OAT, SOY};
	private Milk    milk;

	//constructor
	public Beverage(String size, String hot, String milk, 
					String id, BigDecimal price, String description) {
		
		super(id, price, description);
	
		switch(size.toUpperCase()) { //input recognized regardless of case
			case "S":
				this.size = Size.S;
			break;
			case "M":
				this.size = Size.M;
			break;
			case "L":
				this.size = Size.L;
			break;
		}
		
		switch(hot.toUpperCase()) {
			case "HOT":
				this.hot  = true;
			break;
			
			case "COLD": 
				this.hot = false;
			break;
		}
			
		switch(milk.toUpperCase()) {
			case "NONE":
				this.milk = Milk.NONE;
			break;
			case "REGULAR":
				this.milk = Milk.REGULAR;
			break;
			case "OAT":
				this.milk = Milk.OAT;
			break;
			case "SOY":
				this.milk = Milk.SOY;
			break;
		}
	}
	
	public Size getSize() {
		return size;
	}
	
	public boolean isHot() {
		return hot;
	}
	
	public Milk getMilk() {
		return milk;
	}
	
	//overrides toStirng() method
	public String toString() {
		String string = "";
		string += super.getID()  + " ";
		string += super.getDescription() + " ";
		string += getMilk() + " ";
		string += getSize() + " £";
		string += super.getPrice();
		return string;
	}
}
