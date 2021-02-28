package sim.coffee;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

public class OrderListTest {
    static OrderList testList;

    @BeforeClass
    public static void init() {
        testList = new OrderList(new LinkedList<>());
    }

    /**
     * Tests that the list successfully creates the number of orders in the input
     * data (assuming they're made right)
     */
    @Test
    public void readFile() throws FileNotFoundException {
        int sizeBefore = testList.size();

        testList.readFile("data/test/orders.csv");

        assertEquals(sizeBefore + 3, testList.size());
    }
}
