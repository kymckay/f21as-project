package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap.KeySet;


public class Menu{

	HashMap<String, MenuItem> Menu;
	
	
	public Menu()
	{
		Menu = new HashMap<String, MenuItem>();				
	}
	
	
		
	public void add(String id, MenuItem m)
	{
		Menu.put(id, m);
	}
	
	
	public void readFile(String fileName) throws FileNotFoundException
	{
		
		File menu = new File("input.txt");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(menu);
		
		while(scan.hasNextLine()) 
		{			
			processLine(scan.nextLine());
		}
	
	}	
	
	
	public void processLine(String line)
	{
		
	}

	
	public String getReport()
	{
		return null;		
	}

	
	public MenuItem getKey(String key)
	{
		return MenuItem.id;
		
	}

	
	public Set<String> keyset()
	{
		return Menu.keySet();
	}

}