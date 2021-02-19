package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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
    
		public void processLine(String Line) // Method to process the file
		{
			try
			{
				if (Line.split(",").length>=2)
				{
					String[] words = Line.split(",");
					
					if (words[2].matches("^B"))
					{
						OrderBeverage orderBeverage = new OrderBeverage(word[3],word[4],word[5],new MenuItem(word[2],
															MenuTableModel.getValueAt(word[2],2)),
															MenuTableModel.getValueAt(word[2],1))
															)); 
						orders.add(new Order(word[0],word[1],orderBeverage,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price
						
					}
					
					
					if (words[2].matches("^F"))
					{

						OrderFood orderFood = new OrderFood(,new MenuItem(word[2],
															MenuTableModel.getValueAt(word[2],2)),
															MenuTableModel.getValueAt(word[2],1))
															)); 
						
						orders.add(new Order(word[0],word[1],orderFood,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price
						
					}

					
					if (words[2].matches("^M"))
					{
						
						OrderMerchandise orderMerchandise = new OrderMerchandise(word[3],word[4],new MenuItem(word[2],
								MenuTableModel.getValueAt(word[2],2)),
								MenuTableModel.getValueAt(word[2],1))
								)); 

						orders.add(new Order(word[0],word[1],orderFood,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price

					}
					
						
				}
			}
		    catch(NumberFormatException e)
			{
				System.out.println("Invalid Line");
				continue;
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
