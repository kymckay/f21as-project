package sim.coffee;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

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

    static Menu testMenu;
    static MenuTableModel testModel = new MenuTableModel(testMenu);

    @BeforeClass
    public static void init() {
        try {
            testMenu = new Menu("data/test/menu.csv");
        } catch (FileNotFoundException e) {
            // Test cannot pass if file was not found
            System.exit(1);
        }
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
