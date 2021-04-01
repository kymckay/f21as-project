package sim.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This class simulates the outside world, producing customers as they arrive
 * and join the queue for staff to serve (in the form of Order groupings)
 *
 * Orders are read in from a supplied CSV file where each line is an item in a customer's order.
 * Order groupings with the same customer are all one "order" and added to the queue together.
 */
public class Producer implements Runnable {
    private File file; // customers come from here
    private SharedQueue out; // customers go here
    private Menu menu; // orders refer to menu

    // Need to log incoming customers
    private Logger log = Logger.getInstance();

    private String frontOfLine; // Customer currently at the front of the line
    private boolean priority; // Is the current customer a priority customer
    private LinkedList<MenuItem> basket = new LinkedList<>(); // Contains orders for customer at front of line

    public Producer(File file, SharedQueue out, Menu menu) {
        this.file = file;
        this.out = out;
        this.menu = menu;
    }

    /**
     * Opens the orders file and starts populating the queue
     */
    @Override
    public void run() {
        // Buffered reader provides an efficient way to read files line by line
        int lineNum = 1;
        try (
            BufferedReader input = new BufferedReader(new FileReader(file));
        ) {
            // Skip over first line (csv column headings)
            String line = input.readLine();

            // Returns null when EOF reached
            while ((line = input.readLine()) != null) {
                lineNum++;

                String[]info  = splitLine(line);
                String name   = info[0];
                String itemId = info[1];

                // Next customer reached, checkout basket before continuing
                if (!name.equals(frontOfLine)) {
                    // Don't checkout on very first iteration
                   if (frontOfLine != null) {
                       // Simulate time between customers arriving
                       sleep(2000l);
                       checkout();
                   }

                    // The customer is now front of line
                    frontOfLine = name;
                    priority = Boolean.parseBoolean(info[2]);

                    // Some random time may pass before the new customer arrives
                    if (Math.random() < 0.5) {
                        sleep((long) Math.floor(Math.random() * 5000l));
                    }
                }

                // Add current order line to basket
                basket.add(menu.getItem(itemId));
                menu.getItem(itemId).setCount();

            }

            // Add final customer's order
            checkout();

            // Once end of file is reached mark the queue as completed
            out.setDone();
        } catch (FileNotFoundException e) {
            System.out.println(file.getName() + " does not exist.");
        } catch (IOException e) {
            System.out.println(file.getName() + " could not be read.");
        } catch(IllegalArgumentException e) {
            System.out.println("Customer parsing error on line " + lineNum + ": " + e.getMessage());
        }
    }

    // Extracted to method for readability and common handling of interruption
    private void sleep(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void checkout() {
        // Produce a new customer from the front of the line and their basket
        Customer newArrival = new Customer(frontOfLine, basket.toArray(MenuItem[]::new));
        basket.clear();

        // Add the customer to the service queue and log the event
        out.add(newArrival, priority ? 0 : 1);
        log.add(String.format("%s joins the %s queue",
            newArrival.getName(),
            priority ? "priority" : "regular"
        ));
    }

    private String[] splitLine(String line) {
        // Remove whitespace while splitting using regex delimiter
        // Java's split operator discards empty strings by default, -1 keeps them (empty
        // csv columns are valid)
        String[] cols = line.split("\\s*,\\s*", -1);

        // All rows in csv file have same columns
        if (cols.length == 3) {
            return cols;
        } else {
            throw new IllegalArgumentException("Expected 3 columns");
        }
    }
}
