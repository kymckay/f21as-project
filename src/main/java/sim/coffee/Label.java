package sim.coffee;

//used in Merchandise and OrderMerchandise objects
public enum Label {
	TEA (0.75), 
	COFFEE (1.5);
	
	private final double addOnPrice; //in pounds (£)
					//final price of a merchandise item is calculated by adding the addOnPrice to the base price
	
	private Label(double addOnPrice) {
		this.addOnPrice = addOnPrice;
	}
	
	public double getAddOnPrice() {
		return addOnPrice;
	}
}
