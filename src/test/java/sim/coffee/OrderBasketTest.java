package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class OrderBasketTest {
    static OrderBasket testBasket;
    static Order testOrder;
    static OrderList testList;

    // If file isn't found tests fail automatically
    @BeforeClass
    public static void init() throws FileNotFoundException {
        testList = new OrderList(new ArrayList<>());

        testBasket = new OrderBasket(
            new Menu("data/test/menu.csv"),
            testList,
            new ArrayList<>()
        );

        testOrder = new Order(
            LocalDateTime.now(),
            "JK001",
            new OrderItem(
                "F001",
                "",
                new BigDecimal("0"),
                new BigDecimal("0")
            )
        );
    }

    @Test
    public void itemAdded() {
        int before = testBasket.size();

        testBasket.add(testOrder);

        assertEquals(before + 1, testBasket.size());
    }

    /**
     * Tests that upon checkout the basket is emptied of all orders
     * and the contents are moved to the historic list
     */
    @Test
    public void checkout() {
        testBasket.add(testOrder);

        int size = testBasket.size();
        assertNotEquals(0, size);

        testBasket.checkout();
        assertEquals(0, testBasket.size());
        assertEquals(size, testList.size());
    }
}
