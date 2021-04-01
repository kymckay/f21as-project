package sim.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    // Don't want millisecond precious for a log file
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static Logger instance;
    private StringBuilder sBuilder;

    private Logger() {
        sBuilder = new StringBuilder();
    }

    /**
     * Logs a new string in the log (adds time and newline)
     * @param string
     */
    public synchronized void add(String string) {
        sBuilder.append(String.format("%s %s%n",
            LocalDateTime.now().format(formatter),
            string
        ));
    }

    /**
     * Logs a new simulation event in the log (adds time and newline)
     * @param event event message where %s will be replaced with the customer name
     * @param customer customer object the event relates to
     */
    public synchronized void add(String event, Customer customer) {
        add(String.format(event, customer.getName()));
    }

    // Lazy-loads the singleton instance of the class
    // Synchronized whole method to avoid double-checked locking
    // (shown in lectures, but discouraged in modern Java practice)
    public static synchronized Logger getInstance() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    //writes log to file
    public void writeReport(String filename) {
        try (FileWriter orderWriter = new FileWriter(filename)) {
            orderWriter.write(sBuilder.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
