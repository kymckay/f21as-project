package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderListTest {
    static OrderBasket testBasket;
    static Order testOrder;
    static OrderList testList;
    static Menu testMenu;

    // If file isn't found tests fail automatically
    @BeforeClass
    public static void init() throws FileNotFoundException {
        testMenu = new Menu("data/test/menu.csv");
        testList = new OrderList(new ArrayList<>());

        testBasket = new OrderBasket(testMenu, testList, new ArrayList<>());

        testOrder = new Order(LocalDateTime.now(), "JK001",
                new OrderItem("F001", "", new BigDecimal("0"), new BigDecimal("0")));
    }

    // Remove any lingering data between tests
    @After
    public void clearBasket() {
        testBasket.checkout();
    }

    private void setupOrder(String isoDate) {
        BigDecimal foodPrice = testMenu.getItem("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getItem("B001").getPrice();

        // These must match the test data menu input
        OrderItem foodItem = new OrderItem("F001", "", foodPrice, foodPrice);
        OrderItem drinkItem = new OrderItem("B001", "L|true|None", drinkPrice, drinkPrice);

        LocalDateTime time = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);

        Order food = new Order(time, "TS001", foodItem);
        Order drink = new Order(time, "TS001", drinkItem);

        testBasket.add(food);
        testBasket.add(drink);
    }

    /**
     * Test to make sure that getDate() only date without time. 
     */
    @Test
    public void checkDate() {
        setupOrder("2021-03-07T09:15Z");

        // testDate only has year, month, day and no time.
        LocalDate testDate = LocalDate.of(2021, 3, 07);

        // getDate() method should only return date without time
        assertEquals(testDate, testBasket.get(1).getDate());
        // getTime() method should return date with time. 
        assertNotEquals(testDate, testBasket.get(1).getTime());
    }
}
