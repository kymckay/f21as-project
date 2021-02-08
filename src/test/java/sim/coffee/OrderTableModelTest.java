package sim.coffee;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for MenuTableModel.
 */
public class OrderTableModelTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException() {
        OrderList testList = new OrderList();
        testList.readFile("data/test/orders.csv");
        OrderTableModel testModel = new OrderTableModel(testList);

        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 3");

        testModel.getValueAt(0, 3);
    }

    /**
     * Ensure table model captures correct number of entries in the order list.
     */
    @Test
    public void rowCount() {
        OrderList testList = new OrderList();
        testList.readFile("data/test/orders.csv");
        OrderTableModel testModel = new OrderTableModel(testList);

        int count = testModel.getRowCount();
        assertEquals(3, count);
    }
}
