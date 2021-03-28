package sim.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import sim.app.Order;
import sim.app.OrderItem;

/**
 * This class simulates the outside world, producing customers as they arrive
 * and join the queue for staff to serve (in the form of Order groupings)
 *
 * Orders are read in from a supplied CSV file where each line is an item in a customer's order.
 * Order groupings with the same customer are all one "order" and added to the queue together.
 */
public class Producer implements Runnable {
    File in; // orders come from here
    SharedQueue out; // orders go here
    SharedQueue priorityOut;

    String frontOfLine; // Customer ID currently at the front of the line
    LinkedList<Order> basket = new LinkedList<>(); // Contains orders for customer at front of line

    public Producer(File in, SharedQueue out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Opens the orders file and starts populating the queue
     */
    @Override
    public void run() {
        // Buffered reader provides an efficient way to read files line by line
        int lineNum = 1;
        try (
            BufferedReader input = new BufferedReader(new FileReader(in));
        ) {
            String line;
            // Returns null when EOF reached
            while ((line = input.readLine()) != null) {
                // A single customer's items go into their basket until they checkout
                // before the next customer
                Order item = processLine(line);

                // Next customer reached, checkout basket and empty
                if (!item.getCustomerID().equals(frontOfLine)) {
                    // Don't checkout if there was no previous customer
                    if (frontOfLine != null) {
                        // Simulate customer takes 2s per item to order
                        sleep(basket.size() * 2000l);

                        // Customer's items are all added as a grouping into the queue ("checkout")
                        out.addOrder(basket.toArray(Order[]::new));
                        basket.clear();
                    }

                    // Some random time may pass before the next customer arrives
                    if (Math.random() < 0.5) {
                        sleep((long) Math.floor(Math.random() * 5000l));
                    }

                    // New customer is now front of line
                    frontOfLine = item.getCustomerID();
                }

                basket.add(item);

                lineNum++;
            }

            // Once end of file is reached mark the queue as completed
            out.setDone();
        } catch (FileNotFoundException e) {
            System.out.println(in.getName() + " does not exist.");
        } catch (IOException e) {
            System.out.println(in.getName() + " could not be read.");
        } catch(IllegalArgumentException e) {
            System.out.println("Order parsing error on line " + lineNum + ": " + e.getMessage());
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

    private Order processLine(String line) {
        // Remove whitespace while splitting using regex delimiter
        // Java's split operator discards empty strings by default, -1 keeps them (empty
        // csv columns are valid)
        String[] cols = line.split("\\s*,\\s*", -1);

        // All rows in csv file have same columns
        if (cols.length == 6) {
            LocalDateTime timestamp = LocalDateTime.parse(cols[0], DateTimeFormatter.ISO_DATE_TIME);
            String custId = cols[1];
            Boolean priority = Boolean.valueOf(cols[2]);
            String itemId = cols[3];
            BigDecimal priceFull = new BigDecimal(cols[4]);
            BigDecimal pricePaid = new BigDecimal(cols[5]);
            String itemDetails = cols[6];

            // OrderItem subclasses store the item permutations ordered
            OrderItem newItem = new OrderItem(itemId, itemDetails, priceFull, pricePaid);

            return new Order(timestamp, custId, newItem, priority);
        } else {
            throw new IllegalArgumentException("Line contains wrong number of values");
        }
    }
}
