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

	LinkedList<Order> orders;	// LinkedList has been declared

	public OrderList()		// Constructor for orderLisr=t is created
	{
		orders= new LinkedList<Order>();
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
			if (line.split(",").length >= 2) {
				String[] cols = line.split(",");

				LocalDateTime timestamp = LocalDateTime.parse(cols[0]);
				String custId = cols[1];
				String itemId = cols[2];

				switch (itemId.substring(0,1)) {
					case "B":
						OrderBeverage orderBeverage = new OrderBeverage(cols[3],cols[4],cols[5],new MenuItem(cols[2],
															MenuTableModel.getValueAt(cols[2],2)),
															MenuTableModel.getValueAt(cols[2],1))
															));
						orders.add(new Order(timestamp, cols[1],orderBeverage,MenuTableModel.getValueAt(cols[2],2))); // use menutable model object and get the price
						break;
					case "F":
						OrderFood orderFood = new OrderFood(,new MenuItem(cols[2],
															MenuTableModel.getValueAt(cols[2],2)),
															MenuTableModel.getValueAt(cols[2],1))
															));

						orders.add(new Order(timestamp, cols[1],orderFood,MenuTableModel.getValueAt(cols[2],2))); // use menutable model object and get the price
						break;
					case "M":
						OrderMerchandise orderMerchandise = new OrderMerchandise(cols[3],cols[4],new MenuItem(cols[2],
								MenuTableModel.getValueAt(cols[2],2)),
								MenuTableModel.getValueAt(cols[2],1))
								));

						orders.add(new Order(timestamp, cols[1],orderFood,MenuTableModel.getValueAt(cols[2],2))); // use menutable model object and get the price
						break;
					default:
						// TODO: Throw exception here
						break;
				}
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

				sum.add(Order.getPricePaid());
			}

			return sum;

		}

		public int size() // Getting size of LinkedList
		{
			return orders.size();
		}
}
