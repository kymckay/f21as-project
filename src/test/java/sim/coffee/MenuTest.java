package sim.coffee;

import static org.junit.Assert.assertEquals;


import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;



import org.junit.Test;

public class MenuTest {

@Test
public void add()
{
	
	try {
		
		String itemDetails   = "DairyFree";
		Menu testMenu;
		testMenu = new Menu("data/test/menu.csv");
		 DietaryClass[] dietaryClasses =
					Arrays.stream(itemDetails.split(Menu.PIPE_SEP))
						.map(String::toUpperCase)
						.map(DietaryClass::valueOf)
						.toArray(DietaryClass[]::new);
		 MenuItem m;
		 m = new Food(dietaryClasses, "F001", new BigDecimal(4.99), "Bacon Sandwich");
		  testMenu.add("F001", m);
			 assertEquals(testMenu.keySet().toString().contains("F001"),true);
		
			 
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	}catch (IllegalIDException e) {
		
		e.printStackTrace();
	}
}  

	 

@Test
public void readFile()
{
	
	try {
		
		String itemDetails   = "DairyFree";
		 Menu testMenu;
		testMenu = new Menu("data/test/menu.csv");
		 
			 assertEquals(testMenu.keySet().toString(),"[B001, M032, F001]");
		
			 
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	}
   
}
}


