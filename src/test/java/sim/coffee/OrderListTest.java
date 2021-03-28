package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import sim.app.Order;
import sim.app.OrderItem;
import sim.app.OrderList;

public class OrderListTest {
    OrderList testList;

    // New empty list for every test
    @Before
    public void init() {
        testList = new OrderList(new LinkedList<>());
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
        LocalDateTime timeToday = LocalDateTime.now().withHour(7);

        // Add an order today, tomorrow and yesterday
        testList.add(testOrder(timeToday));
        testList.add(testOrder(timeToday.minusDays(1)));
        testList.add(testOrder(timeToday.plusDays(1)));

        BigDecimal dayIncome = testList.getDayIncome(timeToday.toLocalDate());

        assertEquals(new BigDecimal("5"), dayIncome);
        assertNotEquals(dayIncome, testList.getTotalIncome());
    }
}
