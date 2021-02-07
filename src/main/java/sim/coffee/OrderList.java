package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OrderList {
public void readFile() {
		
		File inputFileObject = new File("input.txt"); 
		Scanner scannerObject;
		
		try {
			String[] lines = new String[20];
			scannerObject = new Scanner(inputFileObject);
			for(int i=0;scannerObject.hasNextLine();i++) {
				lines[i]=scannerObject.nextLine();
			}
			//XXList.readxxListFile(lines);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
			    
	}
    
}
