package sim.coffee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * This class simulates the outside world, producing customers as they arrive
 * and join the queue for staff to serve (in the form of Order groupings)
 *
 * Orders are read in from a supplied CSV file where each line is an item in a customer's order.
 * Order groupings with the same customer are all one "order" and added to the queue together.
 */
public class Producer implements Runnable {
    File in; // orders come from here
    Menu menu; // used to produce orders
    Queue out; // orders go here

    String frontOfLine; // Customer ID currently at the front of the line
    LinkedList<Order> basket = new LinkedList<>(); // Contains orders for customer at front of line

    public Producer(File in, Menu menu, Queue out) {
        this.in = in;
        this.menu = menu;
        this.out = out;
    }

    /**
     * Opens the orders file and starts populating the queue
     */
    @Override
    public void run() {
        // Sleep some time based on the number of items to order (simulate customer deciding)
        // Put orders for one customer into the queue at once (simulates "checkout")
        // Sleep randomly between each customer (simulate time between arrival)

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
                if (item.getCustomerID().equals(frontOfLine)) {
                    out.addOrder(basket.toArray()); // TODO: This will add en empty array first iteration
                    basket.clear();
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

    private Order processLine(String line) {
        // Remove whitespace while splitting using regex delimiter
        // Java's split operator discards empty strings by default, -1 keeps them (empty
        // csv columns are valid)
        String[] cols = line.split("\\s*,\\s*", -1);

        // All rows in csv file have same columns
        if (cols.length == 6) {
            LocalDateTime timestamp = LocalDateTime.parse(cols[0], DateTimeFormatter.ISO_DATE_TIME);
            String custId = cols[1];
            String itemId = cols[2];
            BigDecimal priceFull = new BigDecimal(cols[3]);
            BigDecimal pricePaid = new BigDecimal(cols[4]);
            String itemDetails = cols[5];

            // OrderItem subclasses store the item permutations ordered
            OrderItem newItem = new OrderItem(itemId, itemDetails, priceFull, pricePaid);

            return new Order(timestamp, custId, newItem);
        } else {
            throw new IllegalArgumentException("Line contains wrong number of values");
        }
    }
}
