package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;
//import java.util.HashMap.KeySet;


public class Menu{

	HashMap<String, MenuItem> Menu;		//Declared HashMap called Menu
	
	
	public Menu()			//Constructor for class Menu
	{
		Menu = new HashMap<String, MenuItem>();		
	}
	
			
	public void add(String id, MenuItem m)		// Method to add String id(key) and MenuItem m(value)	
	{
		Menu.put(id, m);
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
	
	
	public void processLine(String line)		//Code for ProcessLine to be brought over from LRbranch
	{
		
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