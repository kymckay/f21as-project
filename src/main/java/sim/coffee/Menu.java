package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    
	
	public void readFile() throws FileNotFoundException
	{
		File file = new File("input.txt");
		Scanner scan = new Scanner(file);
		
		while(scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
	
	}	
}
