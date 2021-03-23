package sim.coffee;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sim.coffee.Beverage;
import sim.coffee.Colour;
import sim.coffee.DietaryClass;
import sim.coffee.Food;
import sim.coffee.IllegalIDException;
import sim.coffee.Label;
import sim.coffee.Merchandise;
import sim.coffee.Milk;
import sim.coffee.Size;

/**
 * JUnit test for IllegalIDException
 */
public class IllegalIDExceptionTest {
	BigDecimal p;
	String d;
	String id;
	boolean h;
	Size[] s;
	Milk[] m;
	DietaryClass[] dc;
	Colour[] c;
	Label[] l;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	/** Ensure invalid Beverage ID format throws an exception
	 *
	 * @throws IllegalIDException
	 */
	@Test
	public void testBeverageConstructor() throws IllegalIDException  {
		id = "H567";
		exceptionRule.expect(IllegalIDException.class);
		exceptionRule.expectMessage("Invalid ID " + id);
		new Beverage(s,h,m,id,p,d);
	}

	/** Ensure invalid Food ID format throws an exception
	 *
	 * @throws IllegalIDException
	 */
	@Test
	public void testFoodConstructor() throws IllegalIDException  {
		id = "F9567";
		exceptionRule.expect(IllegalIDException.class);
		exceptionRule.expectMessage("Invalid ID " + id);
		new Food(dc,id,p,d);
	}

	/** Ensure invalid Merchandise ID format throws an exception
	 *
	 * @throws IllegalIDException
	 */
	@Test
	public void testMerchandiseConstructor() throws IllegalIDException  {
		id = "MF67";
		exceptionRule.expect(IllegalIDException.class);
		exceptionRule.expectMessage("Invalid ID " + id);
		new Merchandise(l,c,id,p,d);
	}

}