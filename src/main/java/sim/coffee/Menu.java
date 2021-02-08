package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
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

}
