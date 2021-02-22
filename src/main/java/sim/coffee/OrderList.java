package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;

public abstract class OrderList {

	LinkedList<Order> orders = new LinkedList<>();

	OrderList() {}

	OrderList(String filename) throws FileNotFoundException {
		readFile(filename);
	}

	// No graceful way to handle a missing file, pass exception up to decide how to
	// proceed in context
	public void readFile(String filename) throws FileNotFoundException {
		File inputFile = new File(filename);

		try (
				Scanner scanner = new Scanner(inputFile);
		) {
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
			}
		}
	}

	private void processLine(String line) {
		// Split regex trims excess whitespace near commas
		String[] cols = line.split("\\s*,\\s*");

		// 4 properties common to all product types
		if (cols.length >= 4) {
			LocalDateTime timestamp = LocalDateTime.parse(cols[0], DateTimeFormatter.ISO_DATE_TIME);
			String custId = cols[1];
			String itemId = cols[2];
			BigDecimal paid = new BigDecimal(cols[3]);

			// OrderItem subclasses store the item permutations ordered
			OrderItem newItem;

			// First character of item ID diferentiates the types
			// Each type has different possible properties in remaining file columns
			switch (itemId.substring(0,1)) {
				case "B":
					Size size = Size.valueOf(cols[4].toUpperCase());
					boolean isHot = Boolean.parseBoolean(cols[5]);
					Milk milk = Milk.valueOf(cols[6].toUpperCase());

					newItem = new OrderBeverage(size, isHot, milk);
					break;
				case "F":
					// Food items are simple
					newItem = new OrderFood();
					break;
				case "M":
					Label label = Label.valueOf(cols[4].toUpperCase());
					Colour colour = Colour.valueOf(cols[5].toUpperCase());

					newItem = new OrderMerchandise(label, colour);
					break;
				default:
					throw new IllegalArgumentException("Line contains invalid Item ID");
			}

			orders.add(new Order(timestamp, custId, newItem, paid));
		} else {
			throw new IllegalArgumentException("Line contains too few values");
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
