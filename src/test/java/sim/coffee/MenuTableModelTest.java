package sim.coffee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for MenuTableModel.
 */
public class MenuTableModelTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException()
    {
        Menu testMenu = new Menu();
        testMenu.readFile("data/test/menu.csv");
        MenuTableModel testModel = new MenuTableModel(testMenu);

        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 3");

        testModel.getValueAt(0, 3);
    }

    /**
     * Ensure table model captures correct number of entries in the menu.
     */
    @Test
    public void rowCount() {
        Menu testMenu = new Menu();
        testMenu.readFile("data/test/menu.csv");
        MenuTableModel testModel = new MenuTableModel(testMenu);

        int count = testModel.getRowCount();
        assertEquals(3, count);
    }
}
