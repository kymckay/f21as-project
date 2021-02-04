package sim.coffee;

//used in Beverage and orderBeverage objects 
public enum Size {
	S (1.0),
	M (1.3),
	L (1.5);

	private final double coefficient; //final price of a beverage is calculated 
									  //by multiplying the base + milk price with the size coefficient 
									  //e.g. price of a  Medium Soy Milk Latte (base price £2.00, Soy Milk £0.10) = (£2.00 + £0.10) * 1.3 = £2.73
	//constructor 
	private Size(double coefficient) {
		this.coefficient = coefficient;
	}
	
	public double getCoefficient() {
		return coefficient;
	}
}
