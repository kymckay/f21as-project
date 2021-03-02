package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class OrderListTest {
    OrderList testList;

    // New empty list for every test
    @Before
    public void init() {
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

    // Helper method to build empty orders for testing
    private Order testOrder(LocalDateTime t) {
        BigDecimal price = new BigDecimal("5");
        return new Order(t, "", new OrderItem("", "", price, price));
    }

    /**
     * Tests that the income sum for a given day correctly excludes orders from
     * other days
     */
    @Test
    public void dayIncome() {
        // Time at 7.00 am since no discount applies at that time
        LocalDateTime timeToday = LocalDateTime.parse("2020-03-08T07:15Z", DateTimeFormatter.ISO_DATE_TIME);

        // Add an order today, tomorrow and yesterday
        testList.add(testOrder(timeToday));
        testList.add(testOrder(timeToday.minusDays(1)));
        testList.add(testOrder(timeToday.plusDays(1)));

        BigDecimal dayIncome = testList.getDayIncome(timeToday.toLocalDate());

        assertEquals(new BigDecimal("5"), dayIncome);
        assertNotEquals(dayIncome, testList.getTotalIncome());
    }
}
