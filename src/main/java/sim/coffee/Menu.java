package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;


public class Menu{
		
	public static void main(String args[]) 
	{
	
		HashMap<Integer, String> menu = new HashMap<Integer, String>();
	
		menu.put(null, null);
		menu.put(null, null);
		menu.put(null, null);
		
	}
	
	
	public void Menu()
	{
	
	}
	
	public Set<Integer> keySet()  //For keySet
    {  
	return menu.keySet();
	}	

/*	Set<String> keys = hmap.keySet();		//For keySet
	System.out.println("keySet is:");
	Iterator<Integer> it = keys.iterator();
		while(it.hasNext()){
		System.out.print(it.next());	*/
	
	
	
	public void add(MenuItem m)
	{
		
	}
	
	
	public void readFile(String fileName) throws FileNotFoundException
	{
		
		File menu = new File("input.txt");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(menu);
		
		while(scan.hasNextLine()) 
		{
			System.out.println(scan.nextLine());
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
		return null;
		
	}
}
