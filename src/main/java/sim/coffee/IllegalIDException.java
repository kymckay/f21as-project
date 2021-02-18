package sim.coffee;

public class IllegalIDException extends Exception {
	
	public IllegalIDException(String id) {
		super("Invalid ID " + id);
	}

}
