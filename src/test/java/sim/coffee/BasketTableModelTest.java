package sim.coffee;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for MenuTableModel.
 */
public class BasketTableModelTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    static OrderBasket testBasket;
    static BasketTableModel testModel;

    // If file isn't found tests fail automatically
    @BeforeClass
    public static void init() throws FileNotFoundException {
        testBasket = new OrderBasket(
            new Menu("data/test/menu.csv"),
            new OrderList(new ArrayList<>()),
            new ArrayList<>()
        );

        testBasket.add(new Order(
            LocalDateTime.now(),
            "JK001",
            new OrderItem(
                "F001",
                "",
                new BigDecimal("0"),
                new BigDecimal("0")
            )
        ));

        testModel = new BasketTableModel(testBasket);
    }

    /**
     * Ensure invalid column indexes throw an exception.
     */
    @Test
    public void invalidIndexThrowsException() {
        // Next method call should throw the exception
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Index out of range: 5");

        testModel.getValueAt(0, 5);
    }

    /**
     * Ensure table model reflects correct number of items in basket.
     */
    @Test
    public void rowCountCorrect() {
        int count = testModel.getRowCount();
        assertEquals(1, count);
    }
}
