package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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

    // Local Variables
    // Using a time in the past for timePast so that there
    // will be no overlapping of the same day as "TODAY" >> see next variable
    LocalDateTime timePast = LocalDateTime.parse("2020-03-07T07:15Z", DateTimeFormatter.ISO_DATE_TIME);
    // "TODAY" --- Both date uses time 7.00 am since no discount applies at that time
    LocalDateTime timeToday = LocalDateTime.parse("2020-03-08T07:15Z", DateTimeFormatter.ISO_DATE_TIME);

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

    // Helper method to add orders to basket for discount testing
    // Always adds a food item and large hot coffee with a date in the past
    // as well as adds a food item and large hot coffee with today's date
    private void setupOrder() {
        BigDecimal foodPrice = testMenu.getItem("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getItem("B001").getPrice();

        // These must match the test data menu input
        OrderItem foodItem = new OrderItem("F001", "", foodPrice, foodPrice);
        OrderItem drinkItem = new OrderItem("B001", "L|true|None", drinkPrice, drinkPrice);

        Order foodNotToday = new Order(timePast, "TS001", foodItem);
        Order drinkNotToday = new Order(timePast, "TS001", drinkItem);
        Order foodToday = new Order(timeToday, "TS001", foodItem);
        Order drinkToday = new Order(timeToday, "TS001", drinkItem);

        testBasket.add(foodNotToday);
        testBasket.add(drinkNotToday);
        testBasket.add(foodToday);
        testBasket.add(drinkToday);
    }

    /**
     * Test to make sure that getDate() only date without time. 
     */
    @Test
    public void checkDate() {
        setupOrder();
        
        // testDate only has year, month, day and no time.
        LocalDate testDate = timeToday.toLocalDate();

        // getDate() method should only return date without time
        assertEquals(testDate, testBasket.get(2).getDate());
        // getTime() method should return date with time. 
        assertNotEquals(testDate, testBasket.get(1).getTime());
    }

    @Test
    public void checkTodayBasketPrice() {
        BigDecimal foodPrice = testMenu.getItem("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getItem("B001").getPrice();
        BigDecimal todayPrice = foodPrice.add(drinkPrice);

        setupOrder();
        assertEquals(todayPrice, testBasket.getTodayIncome(timeToday.toLocalDate()));
        assertNotEquals(todayPrice, testBasket.getTotalIncome());
    }
}
