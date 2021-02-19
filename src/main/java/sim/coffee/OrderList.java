package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;

public class OrderList {

	LinkedList<Order> OrderList;	
	// LinkedList has been declared
	
	public OrderList()		// Constructor for orderLisr=t is created
	{
		OrderList= new LinkedList<Order>();
	}	
	
	public void readFile(String fileName) {  /* 1. Is process line method same as that in menu class
											    2. Method to read CSV file*/
		
		File inputFileObject = new File("input.txt"); 
		Scanner scannerObject;
		
		try {
			
			scannerObject = new Scanner(inputFileObject);
			for(int i=0;scannerObject.hasNextLine();i++)
			{
				processLine(scannerObject.nextLine());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		}
    
		public void processLine(String Line)
		{
			try
			{
				if (Line.split(",").length>=2)
				{
					String[] words = Line.split(",");
					
					if (words[3].matches("^B"))
					{
						OrderBeverage orderBeverage = new OrderBeverage(word[3],word[4],word[5],new MenuItem(word[2],
															MenuTableModel.getValueAt(word[2],2)),
															MenuTableModel.getValueAt(word[2],1))
															)); 
						OrderList.add(new Order(word[0],word[1],orderBeverage,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price
						
					}
					
					
					if (words[4].matches("^F"))
					{

						OrderFood orderFood = new OrderFood(,new MenuItem(word[2],
															MenuTableModel.getValueAt(word[2],2)),
															MenuTableModel.getValueAt(word[2],1))
															)); 
						
						OrderList.add(new Order(word[0],word[1],orderFood,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price
						
					}

					
					if (words[5].matches("^M"))
					{
						
						OrderMerchandise orderMerchandise = new OrderMerchandise(word[3],word[4],new MenuItem(word[2],
								MenuTableModel.getValueAt(word[2],2)),
								MenuTableModel.getValueAt(word[2],1))
								)); 

						OrderList.add(new Order(word[0],word[1],orderFood,MenuTableModel.getValueAt(word[2],2))); // use menutable model object and get the price

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
			return OrderList.add(o);
			
		}
		
		public String getReport() // What should get report return
		{
			return null;
			
		}
		
		
		public BigDecimal getTotalIncome() // Will this be implemented in GUI ?
		{
			BigDecimal sum = 0;
			
			for(int i=0;i<OrderList.size();i++)
			{
				sum += Order.getPricePaid(); // sum +?d
			}
			return sum;
			
		}
		
		public int size()
		{
			return OrderList.size();
		}
}
