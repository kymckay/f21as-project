package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;

public class OrderList {

	LinkedList<String> OrderList;		// LinkedList has been declared
	
	public OrderList()		// Constructor for orderLisr=t is created
	{
		OrderList= new LinkedList<String>();
	}	
	
	public void readFile(String fileName) {  /* 1. Is process line method same as that in menu class
											    2. Method to read CSV file*/
		
		File inputFileObject = new File("input.txt"); 
		Scanner scannerObject;
		
		try {
			String[] lines = new String[20];
			scannerObject = new Scanner(inputFileObject);
			for(int i=0;scannerObject.hasNextLine();i++)
			{
				lines[i]=scannerObject.nextLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		}
    
		public void processLine(String Line)
		{
			
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
			return OrderList.add(null);
			
		}
		
		public String getReport() // What should get report return
		{
			return null;
			
		}
		
		
		public BigDecimal getTotalIncome() // Will this be implemented in GUI ?
		{
			return null;
			
		}
}
