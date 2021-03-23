package sim.coffee;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import sim.app.Order;
import sim.app.OrderItem;

public class OrderTest {
    /**
     * Test to make sure that getDate() only date without time.
     */
    @Test
    public void checkDate() {
        BigDecimal price = new BigDecimal("0");
        Order o = new Order(LocalDateTime.now(), "", new OrderItem("", "", price, price));

        // testDate only has year, month, day and no time.
        LocalDate testDate = LocalDate.now();

        // getDate() method should only return date without time
        assertEquals(testDate, o.getDate());
    }
}
