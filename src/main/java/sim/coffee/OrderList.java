package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class OrderList {

	List<Order> orders;

	protected OrderList(List<Order> list) { // constructor accepts both LinkedList and ArrayList instances
		orders = list;
	}

	OrderList(String filename, List<Order> list) throws FileNotFoundException {

		orders = list;
		readFile(filename);
	}

	// No graceful way to handle a missing file, pass exception up to decide how to
	// proceed in context
	public void readFile(String filename) throws FileNotFoundException {
		File inputFile = new File(filename);

		int line = 1;

		try (
			Scanner scanner = new Scanner(inputFile);
		) {
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
				line++;
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Parsing error on line " + line + ": " + e.getMessage());
		}
	}

	private void processLine(String line) {
		// Split regex trims excess whitespace near commas
		String[] cols = line.split("\\s*,\\s*");

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

			orders.add(new Order(timestamp, custId, newItem));
		} else {
			throw new IllegalArgumentException("Line contains wrong number of values");
		}
	}

	// Simple wrapper method
	public boolean add(Order o) {
		return orders.add(o);
	}

	// dumps basket to OrderList
	public boolean addAll(List<Order> l) {
		return orders.addAll(l);
	}

	// Simple wrapper method
	public Order get(int index) {
		return orders.get(index);
	}

	// general getter method for income
	public BigDecimal getTotalIncome() {

		// create variable to be returned after looping through orders
		BigDecimal sum = new BigDecimal("0");

		// Loop through all the orders
		for (Order o : orders) {
			sum = sum.add(o.getPricePaid());
		}

		// No rounding needed as prices already rounded
		return sum;
	}

	/**
	 * Income method for the day, 
	 * @return cummulative income for orders received for a specific day
	 */
	public BigDecimal getTodayIncome(LocalDate date) {

		// create variable to be returned after looping through orders
		BigDecimal sum = new BigDecimal("0");

		// Loop through all the orders
		for (Order o : orders) {
			// only add the income for orders that are
			// from the same day the report is being created
			if (o.getDate().equals(date)) {
				sum = sum.add(o.getPricePaid());
			}
		}

		// No rounding needed as prices already rounded
		return sum;

	}

	public int size() {
		return orders.size();
	}
}
