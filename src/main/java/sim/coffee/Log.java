package sim.coffee;

public class Log {
	private static Log instance;
	private StringBuilder log;
	
	private Log() {
		log = new StringBuilder();
	}
	
	public void addOrder(StringBuilder order) {
		log.append(order + "\n");
	}
	
	public static Log getInstance() {
		if (instance == null)
			instance = new Log();
		return instance;
	}
}
