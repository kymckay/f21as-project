package sim.coffee;

//used in Beverage and OrderBeverage objects
public enum Milk {
	NONE (0), 
	REGULAR (0),
	SOY (0.1),
	OAT (0.15);
	
	private final double addOnPrice; //in pounds (£)
					//addOnPrice is added to the beverage's base price prior to multiplying with the size coefficient 
					//see Size class for an example
	
	private Milk(double addOnPrice) {
		this.addOnPrice = addOnPrice;
		
	}
	
	public double getAddOnPrice() {
		return addOnPrice;
	}
}
