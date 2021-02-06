package sim.coffee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for MenuTableModel.
 */
public class MenuTableModelTest
{
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException()
    {
        Menu testMenu = new Menu();
        testMenu.readFile("data/menu.csv");
        MenuTableModel testModel = new MenuTableModel(testMenu);

        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 3");

        testModel.getValueAt(0, 3);
    }
}
