package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;


public class Menu {
	
	public static void main(String args[]) 
	{
	
		HashMap<Integer, String> menu = new HashMap<Integer, String>();
	
		menu.put(null, null);
		menu.put(null, null);
		menu.put(null, null);
		
		System.out.println("Keyset:" + menu.keySet());
	
		/*	public Set<Integer> keySet() {
	    return menu.keySet();
	  	}	*/
	
		//	Set<String> keys = hmap.keySet();
	
		/*	Iterator<Integer> it = keys.iterator();
	  	while(it.hasNext()){
	  	System.out.print(it.next());	*/
	
	}
    
	
	
	public void readFile() throws FileNotFoundException
	{
		
		File file = new File("input.txt");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(file);
		
		while(scan.hasNextLine()) 
		{
			System.out.println(scan.nextLine());
		}
	
	}
	
	public void processLine (String line) {
		
		
		try {
			String details [] = line.split(","); // a local array list with object from the input line separated by a comma
			
			String id = details[0];
			String description = details[1];
			BigDecimal basePrice = new BigDecimal(details[2]);
			
			if (details[0].matches("^B")) {
				
				boolean isHot = Boolean.parseBoolean(details[3]);
				
				String[] size    = details[4].split("|");
				Size[] sizeEnums = new Size[size.length - 1];
				for(int i = 0; i < size.length; i++) {
					sizeEnums[i] = Size.valueOf(size[i].toUpperCase());
				}
				
				String[] milk      = details[5].split("|");
				Milk[]   milkEnums = new Milk[milk.length - 1];
				for(int i = 0; i < milk.length; i++) {
					milkEnums[i] = Milk.valueOf(milk[i].toUpperCase());
				}
				
				Beverage beverage = new Beverage(sizeEnums, isHot, milkEnums, id, basePrice, description);
				
			} else if (details[0].matches("^F")) {
				String[] dietaryClass = details[3].split("|");
				DietaryClass[] dietaryEnums = new DietaryClass[dietaryClass.length - 1];
				for(int i = 0; i < dietaryClass.length; i++) {
					dietaryEnums[i] = DietaryClass.valueOf(dietaryClass[i].replace("\\s","").toUpperCase());
				}
				
				Food food = new Food(dietaryEnums, id, basePrice, description);
				
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

}
