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
public class OrderTableModelTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    static OrderList testList;
    static OrderTableModel testModel = new OrderTableModel(testList);

    @BeforeClass
    public static void init() {
        try {
            testList = new OrderList("data/test/orders.csv");
        } catch (FileNotFoundException e) {
            // Test cannot pass if file was not found
            System.exit(1);
        }
    }

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException() {
        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 3");

        testModel.getValueAt(0, 3);
    }

    /**
     * Ensure table model captures correct number of entries in the order list.
     */
    @Test
    public void rowCountCorrect() {
        int count = testModel.getRowCount();
        assertEquals(3, count);
    }
}
