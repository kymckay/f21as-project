package sim.coffee;

public class IllegalIDException extends Exception {

	// This is just to placate the linter. We never seralise this class so it's
	// irrelevant to us.
	private static final long serialVersionUID = 1L;

	public IllegalIDException(String id) {
		super("Invalid ID " + id);
	}

}
