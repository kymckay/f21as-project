package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class Menu {

	HashMap<String, MenuItem> menuMap = new HashMap<>();

	public Menu(String filename) throws FileNotFoundException {
		readFile(filename);
	}

	public void add(String id, MenuItem m) {
		menuMap.put(id, m);
	}

	// No graceful way to handle a missing file, pass exception up to decide how to
	// proceed in context
	public void readFile(String filename) throws FileNotFoundException {
		File inputFile = new File(filename);

		try (
			Scanner scan = new Scanner(inputFile);
		) {
			while (scan.hasNextLine()) {
				processLine(scan.nextLine());
			}
		}
	}

	private void processLine (String line) {


		try {
			// Splitting with regex trims excess whitespace near commas
			String details [] = line.split("\\s*,\\s*");

			String id            = details[0];
			String description   = details[1];
			BigDecimal basePrice = new BigDecimal(details[2]);

			if (details[0].matches("^B")) { //identify which MenuItem subclass the input line belongs to based on the id

				boolean isHot = Boolean.parseBoolean(details[3]); //need to add boolean isHot values to the sample data

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
					dietaryEnums[i] = DietaryClass.valueOf(dietaryClass[i].replace("\\s","").toUpperCase()); //remove whitespace in the input
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
			System.exit(3);
		}

	}


	public String getReport()
	{
		return null;
	}


	public MenuItem getKey(String key)		//Method to get key. Key is the id from MenuItem??
	{
		return menuMap.get(key);
	}


	public Set<String> keySet()		//keyset method to return set of keys.
	{
		return menuMap.keySet();
	}

}
