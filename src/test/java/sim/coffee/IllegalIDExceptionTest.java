package sim.coffee;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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


	@After
	public void tearDown() throws Exception {
		id = null;
	}

	/** Ensure invalid Beverage ID format throws an exception
	 * 
	 * @throws IllegalIDException
	 */
	@Test
	public void testBeverageConstructor() throws IllegalIDException  {
		id = "H567";
		exceptionRule.expect(IllegalIDException.class);
		exceptionRule.expectMessage("Invalid ID " + id);
		Beverage b = new Beverage(s,h,m,id,p,d);
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
		Food f = new Food(dc,id,p,d);	
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
		Merchandise m = new Merchandise(l,c,id,p,d);	
	}

}