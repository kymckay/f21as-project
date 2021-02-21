package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class Menu {
	// Repeatedly used to split pipe seperated strings
	static final String PIPE_SEP = "\\s*|\\s*";

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

		// Track parsed lines for useful error output
		int line = 1;

		try (
			Scanner scan = new Scanner(inputFile);
		) {
			while (scan.hasNextLine()) {
				processLine(scan.nextLine());
				line++;
			}
		}
		catch (IllegalIDException e) {
			System.out.println("Parsing error on line " + line + ": " + e.getMessage());
		}
	}

	private void processLine (String line) throws IllegalIDException {
		// Splitting with regex trims excess whitespace near commas
		String details [] = line.split("\\s*,\\s*");

		String id            = details[0];
		String description   = details[1];
		BigDecimal basePrice = new BigDecimal(details[2]);

		MenuItem newItem;

		// First character of item ID diferentiates the types
		switch (id.substring(0,1)) {
			case "B":
				boolean isHot = Boolean.parseBoolean(details[3]);

				Size[] sizes =
					Arrays.stream(details[4].split(PIPE_SEP))
						.map(String::toUpperCase)
						.map(Size::valueOf)
						.toArray(Size[]::new);

				Milk[] milks =
					Arrays.stream(details[5].split(PIPE_SEP))
						.map(String::toUpperCase)
						.map(Milk::valueOf)
						.toArray(Milk[]::new);

				newItem = new Beverage(sizes, isHot, milks, id, basePrice, description);

				break;
			case "F":
				DietaryClass[] dietaryClasses =
					Arrays.stream(details[3].split(PIPE_SEP))
						.map(String::toUpperCase)
						.map(DietaryClass::valueOf)
						.toArray(DietaryClass[]::new);

				newItem = new Food(dietaryClasses, id, basePrice, description);
				break;
			case "M":
				Label[] labels =
					Arrays.stream(details[3].split(PIPE_SEP))
						.map(String::toUpperCase)
						.map(Label::valueOf)
						.toArray(Label[]::new);

				Colour[] colours =
					Arrays.stream(details[4].split(PIPE_SEP))
						.map(String::toUpperCase)
						.map(Colour::valueOf)
						.toArray(Colour[]::new);

				newItem = new Merchandise(labels, colours, id, basePrice, description);
				break;
			default:
				throw new IllegalArgumentException("Line contains invalid Item ID");
		}

		this.add(id, newItem);
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
