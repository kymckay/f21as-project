package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Scanner;

public class OrderList {

	LinkedList<Order> orders = new LinkedList<>();

	public OrderList(String filename) {
		readFile(filename);
	}

	public void readFile(String fileName) {  // Method to read the file

		File inputFileObject = new File("input.txt");
		Scanner scannerObject;

		try {

			scannerObject = new Scanner(inputFileObject);
			while(scannerObject.hasNextLine()) {

				processLine(scannerObject.nextLine());

			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void processLine(String line) {
		// Split regex trims excess whitespace near commas
		String[] cols = line.split("\\s*,\\s*");

		// 4 properties common to all product types
		if (cols.length >= 4) {
			LocalDateTime timestamp = LocalDateTime.parse(cols[0]);
			String custId = cols[1];
			String itemId = cols[2];
			BigDecimal paid = new BigDecimal(cols[3]);

			// OrderItem subclasses store the item permutations ordered
			OrderItem newItem;

			// First character of item ID diferentiates the types
			// Each type has different possible properties in remaining file columns
			switch (itemId.substring(0,1)) {
				case "B":
					Size size = Size.valueOf(cols[4]);
					boolean isHot = Boolean.parseBoolean(cols[5]);
					Milk milk = Milk.valueOf(cols[6]);

					newItem = new OrderBeverage(size, isHot, milk);
					break;
				case "F":
					// Food items are simple
					newItem = new OrderFood();
					break;
				case "M":
					Label label = Label.valueOf(cols[4]);
					Colour colour = Colour.valueOf(cols[5]);

					newItem = new OrderMerchandise(label, colour);
					break;
				default:
					// TODO: Throw exception here
					break;
			}

			orders.add(new Order(timestamp, custId, newItem, paid));
		} else {
			// TODO: Throw exception here
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

	public boolean add(Order o)   // Method to add values to the LinkedList
	{
		return orders.add(o);

	}

	public String getReport()
	{
		return null;

	}


	public BigDecimal getTotalIncome() // Method to get the total income
	{
		BigDecimal sum = new BigDecimal(0.0);

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
