package sim.coffee;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Logger {
	
	//enums for the add() method
	public enum OrderState {
		ENTER,
		EXIT,
		PROCESSED;
	}
	
	private static Logger instance;
	private StringBuilder log;

	private Logger() {
		log = new StringBuilder();
	}

	//adds an entry in the log when an order is added to queue (enter), removed from queue (exit) or processed (processed)
	public void add(Order[] o, Logger.OrderState state) {
		LocalDate time = LocalDate.now();
		log.append(String.format("%-15s", time));
		
		switch (state) {
		case ENTER:
			log.append(String.format("%-15s", "Enter queue"));
			break;
		case EXIT:
			log.append(String.format("%-15s", "Exit queue"));
			break;
		case PROCESSED:
			log.append(String.format("%-15s", "Processed"));
			break;
		}

		log.append(String.format("%-25s", "Customer ID: " + o[0].getCustomerID()));
		log.append(String.format("%-7s", "Order: "));
		for (Order order : o) {
			log.append(order.getItemDetails() + " ");
		}
		log.append("\n");
	}


	//returns instance of the Log class
	public static Logger getInstance() {
		if (instance == null)
			instance = new Logger();
		return instance;
	}

	public StringBuilder getLog() {
		return log;
	}

	//writes log to file
	public void writeReport(String filename) {
        try (FileWriter orderWriter = new FileWriter(filename)) {
			orderWriter.write(log.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
