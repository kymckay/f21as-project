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

    private String frontOfLine; // Customer currently at the front of the line
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

                // A single customer's items go into their basket until they checkout
                // before the next customer
                String name = processLine(line);

                // Next customer reached, checkout basket and empty
                if (!name.equals(frontOfLine)) {
                    // Don't checkout if there was no previous customer
                    if (frontOfLine != null) {
                        // Simulate customer takes 2s per item to order
                        sleep(basket.size() * 2000l);

                        checkout();
                    }

                    // Some random time may pass before the next customer arrives
                    if (Math.random() < 0.5) {
                        sleep((long) Math.floor(Math.random() * 5000l));
                    }

                    // New customer is now front of line
                    frontOfLine = name;
                }
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

    // Produce a new customer from the front of the line and their basket
    private void checkout() {
        out.add(new Customer(frontOfLine, basket.toArray(MenuItem[]::new)));
        basket.clear();
    }

    private String processLine(String line) {
        // Remove whitespace while splitting using regex delimiter
        // Java's split operator discards empty strings by default, -1 keeps them (empty
        // csv columns are valid)
        String[] cols = line.split("\\s*,\\s*", -1);

        // All rows in csv file have same columns
        if (cols.length == 2) {
            String name = cols[0];
            String itemId = cols[1];

            basket.add(menu.getItem(itemId));

            return name;
        } else {
            throw new IllegalArgumentException("Expected 2 columns");
        }
    }
}
