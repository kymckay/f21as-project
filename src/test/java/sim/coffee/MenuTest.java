package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

import org.junit.Test;

import sim.coffee.DietaryClass;
import sim.coffee.Food;
import sim.coffee.IllegalIDException;
import sim.coffee.Menu;
import sim.coffee.MenuItem;

public class MenuTest {

@Test
public void add() throws FileNotFoundException,IllegalIDException {

		Menu testMenu = new Menu("data/test/menu.csv");
	    MenuItem m = new Food(new DietaryClass[0], "F001", new BigDecimal(4.99), "Bacon Sandwich");
		testMenu.add("F001", m);
		assertTrue(testMenu.keySet().contains("F001"));
		assertEquals(m, testMenu.getItem("F001"));
}



@Test
public void readFile() throws FileNotFoundException {

		 Menu testMenu = new Menu("data/test/menu.csv");
		 assertEquals(testMenu.keySet().toString(),"[B001, M032, F001]");
	}
}

