package sim.coffee;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for MenuTableModel.
 */
public class MenuTableModelTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    static Menu testMenu = new Menu();
    static MenuTableModel testModel = new MenuTableModel(testMenu);

    @BeforeClass
    public static void init() {
        testMenu.readFile("data/test/menu.csv");
    }

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException()
    {

        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 3");

        testModel.getValueAt(0, 3);
    }

    /**
     * Ensure table model captures correct number of entries in the menu.
     */
    @Test
    public void rowCountCorrect() {
        int count = testModel.getRowCount();
        assertEquals(3, count);
    }
}
