package sim.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public class Menu {
    HashMap<String, MenuItem> menuMap = new HashMap<>();

    public Menu(String filename) {
        readFile(filename);
    }

    // No graceful way to handle a missing file, pass exception up to decide how to
    // proceed in context
    public void readFile(String filename) {
        File file = new File(filename);

        // Track parsed lines for useful error output
        int lineNum = 1;

        try (
            BufferedReader input = new BufferedReader(new FileReader(file));
        ) {
            // Skip over first line (csv column headings)
            String line = input.readLine();

            while ((line = input.readLine()) != null) {
                lineNum++;

                processLine(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(file.getName() + " does not exist.");
        } catch (IOException e) {
            System.out.println(file.getName() + " could not be read.");
        } catch (IllegalArgumentException e) {
            System.out.println("Menu parsing error on line " + lineNum + ": " + e.getMessage());
        }
    }

    private void processLine (String line) {
        // Splitting with regex trims excess whitespace near commas
        // Java's split operator discards empty strings by default, -1 keeps them (empty
        // csv columns are valid)
        String[] details = line.split("\\s*,\\s*", -1);

        // All rows in csv file have same columns
        if (details.length == 4) {
            String id = details[0];
            BigDecimal price = new BigDecimal(details[1]);
            String name = details[2];
            // TODO
            // String options = details[3];

            menuMap.put(id, new MenuItem(id, price, name));
        } else {
            throw new IllegalArgumentException("Expected 4 columns");
        }
    }

    public MenuItem getItem(String key) {
        return menuMap.get(key);
    }

    public Set<String> keysSet() {
        return menuMap.keySet();
    }
}
