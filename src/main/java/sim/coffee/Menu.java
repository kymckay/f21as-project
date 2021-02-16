package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;
//import java.util.HashMap.KeySet;


public class Menu{

	HashMap<String, MenuItem> menu;		//Declared HashMap called Menu
	
	
	public Menu()			//Constructor for class Menu
	{
		menu = new HashMap<String, MenuItem>();		
	}
	
			
	public void add(String id, MenuItem m)		// Method to add String id(key) and MenuItem m(value)	
	{
		menu.put(id, m);
	}
	
	
	public void readFile(String fileName) throws FileNotFoundException		//Method to read the csv file
	{
		
		File menu = new File("input.txt");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(menu);
		
		while(scan.hasNextLine()) 
		{			
			processLine(scan.nextLine());		//This should call the processLine method to process the file
		}
	

	}
	
	public void processLine (String line) {
		
		
		try {
			String details [] = line.split(","); // a local array list with object from the input line separated by a comma
			
			String id = details[0];
			String description = details[1];
			BigDecimal basePrice = new BigDecimal(details[2]);
			
			if (details[0].matches("^B")) { //identify which MenuItem subclass the input line belongs to based on the id
				
				boolean isHot = Boolean.parseBoolean(details[3]);
				
				String[] size    = details[4].split("|"); // a local array list with object from the input line separated by |
				Size[] sizeEnums = new Size[size.length - 1];
				for(int i = 0; i < size.length; i++) { //iterate through all the size array's elements and add them to sizeEnums array
					sizeEnums[i] = Size.valueOf(size[i].toUpperCase());
				}
				
				String[] milk      = details[5].split("|");
				Milk[]   milkEnums = new Milk[milk.length - 1];
				for(int i = 0; i < milk.length; i++) {
					milkEnums[i] = Milk.valueOf(milk[i].toUpperCase());
				}
				
				Beverage beverage = new Beverage(sizeEnums, isHot, milkEnums, id, basePrice, description);
				this.add(id, beverage);
				
			} else if (details[0].matches("^F")) {
				String[] dietaryClass = details[3].split("|");
				DietaryClass[] dietaryEnums = new DietaryClass[dietaryClass.length - 1];
				for(int i = 0; i < dietaryClass.length; i++) {
					dietaryEnums[i] = DietaryClass.valueOf(dietaryClass[i].replace("\\s","").toUpperCase());
				}
				
				Food food = new Food(dietaryEnums, id, basePrice, description);
				this.add(id, food);
				
			} else if (details[0].matches("^M")) {
				String[] label = details[3].split("|");
				Label[] labelEnums = new Label[label.length - 1];
				for(int i = 0; i < label.length; i++) {
					labelEnums[i] = Label.valueOf(label[i].toUpperCase());
				}
				
				String[] colour = details[4].split("|");
				Colour[] colourEnums = new Colour[colour.length - 1];
				for(int i = 0; i < colour.length; i++) {
					colourEnums[i] = Colour.valueOf(colour[i].toUpperCase());
				}
				
				Merchandise merch = new Merchandise(labelEnums, colourEnums, id, basePrice, description);
				this.add(id, merch);
			} 

		}
		
		catch (NumberFormatException nfe) {
			String error = "Number formatting error in line  '" + line;
			System.out.println(error);
			System.exit(0);
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			String error = "Array Index Out Of Bounds error: " + e.getMessage();
			System.out.println(error);
			System.exit(1);
		}
		
		catch (NullPointerException npe)  {
			System.out.println(npe.getMessage());
			System.exit(2);
		}
		
		catch (IllegalIDException iie) {
			System.out.println(iie.getMessage());
		}
	}

	
	public String getReport()
	{
		return null;		
	}

	
	public MenuItem getKey(String key)		//Method to get key. Key is the id from MenuItem??
	{
		return MenuItem.id;
		
	}

	
	public Set<String> keyset()		//keyset method to return set of keys.
	{
		return Menu.keySet();
	}

}