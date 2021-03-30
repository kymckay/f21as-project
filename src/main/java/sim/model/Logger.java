package sim.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	// Don't want millisecond precious for a log file, space at end to distinguish column
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss ");

	//enums for the add() method
	public enum OrderState {
		ENTER,
		EXIT,
		PROCESSED,
		ENTERKITCHEN,
		EXITKITCHEN,
		SERVED;
	}

	private static Logger instance;
	private StringBuilder log;

	private Logger() {
		log = new StringBuilder();
	}

	//adds an entry in the log when an order is added to queue (enter), removed from queue (exit) or processed (processed)
	public synchronized void add(Customer c, OrderState state, QueueType q) {
		log.append(LocalDateTime.now().format(formatter));

		switch (state) {
		case ENTER:
			switch (q) {
				case CUSTOMER:
					log.append(String.format("%-24s", "Enter Normal queue"));
					break;
				default:
					log.append(String.format("%-24s", "Enter Priority queue"));
					break;
			}
			break;
		case EXIT:
			switch (q) {
				case CUSTOMER:
					log.append(String.format("%-24s", "Exit Normal queue"));
					break;
				default:
					log.append(String.format("%-24s", "Exit Priority queue"));
					break;
			}
			break;
		case PROCESSED:
			switch (q) {
				case CUSTOMER:
					log.append(String.format("%-24s", "Processed"));
					break;
				default:
					log.append(String.format("%-24s", "Processed Priority"));
					break;
			}
			break;

		case ENTERKITCHEN:
			log.append(String.format("%-24s", "Enter kitchen"));
			break;

		case EXITKITCHEN:
			log.append(String.format("%-24s", "Exit kitchen"));
			break;

		case SERVED:
			log.append(String.format("%-24s", "Served"));
			break;
		}

		log.append(String.format("%-25s", "Customer: " + c.getName()));
		for (MenuItem item : c.getOrder()) {
			log.append(item.getName() + " ");
		}
		log.append("\n");
	}

	// Laze-loads the singleton instance of the class
	// Synchronized whole method to avoid double-checked locking
	// (shown in lectures, but discouraged in modern Java practice)
	public static synchronized Logger getInstance() {
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
