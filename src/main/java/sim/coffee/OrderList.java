package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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

	public void writeFile(String variable) // Method to write to output file
	{
		FileWriter writeFile;

		try {
			writeFile = new FileWriter("output.txt");
			writeFile.write(variable);
			writeFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Simple wrapper method
	public boolean add(Order o) {
		return orders.add(o);
	}

	public boolean addAll(List<Order> l) {
		return orders.addAll(l);
	}

	// Simple wrapper method
	public Order get(int index) {
		return orders.get(index);
	}

	public String getReport()
	{
		return null;

	}


	public BigDecimal getTotalIncome() // Method to get the total income
	{
		BigDecimal sum = new BigDecimal("0");

		for (Order o : orders) {
			sum = sum.add(o.getPricePaid());
		}

		return sum;

	}

	public int size() // Getting size of LinkedList
	{
		return orders.size();
	}
}
