package sim.coffee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderBasketTest {
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

    /**
     * Tests that upon adding a new order the basket size increases
     */
    @Test
    public void itemAdded() {
        int sizeBefore = testBasket.size();

        testBasket.add(testOrder);

        // The basket should contain one more item
        assertEquals(sizeBefore + 1, testBasket.size());
    }

    /**
     * Tests that upon adding a new order the menu item order tally is incremented
     * (tallying may be moved to checkout in future)
     */
    @Test
    public void itemTallyIncrease() {
        int tallyBefore = testMenu.getKey("F001").getOrderCount();

        testBasket.add(testOrder);

        // The corresponding item should be tallied once
        assertEquals(tallyBefore + 1, testMenu.getKey("F001").getOrderCount());
    }

    /**
     * Tests that upon checkout the basket is emptied of all orders and the contents
     * are moved to the historic list
     */
    @Test
    public void checkout() {
        testBasket.add(testOrder);

        int size = testBasket.size();
        int listSize = testList.size();

        // Basket should successfully contain something before it can checkout
        assertNotEquals(0, size);

        testBasket.checkout();

        // The basket should now be cleared after checkout
        assertEquals(0, testBasket.size());
        // The order list should have grown by the corresponding size
        assertEquals(listSize + size, testList.size());
    }

    // Helper method to add orders to basket for discount testing
    // Always adds a food item and large hot coffee which should cover all discounts
    // Time of day changes via input
    private void setupOrder(String isoDate) {
        BigDecimal foodPrice = testMenu.getKey("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getKey("B001").getPrice();

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
     * Tests that any sandwich and hot drink combo is 30% off between 08:00 and 11:00
     */
    @Test
    public void discount1() {
        BigDecimal foodPrice = testMenu.getKey("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getKey("B001").getPrice();

        BigDecimal factor = new BigDecimal("0.7");

        // Prices are rounded before added
        foodPrice = foodPrice.multiply(factor).setScale(2, RoundingMode.HALF_EVEN);
        drinkPrice = drinkPrice.multiply(factor).setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal discountPrice = drinkPrice.add(foodPrice);

        setupOrder("2021-03-07T09:15Z");
        assertEquals(discountPrice, testBasket.getTotalIncome());
    }

    /**
     * Tests that any drink and food item ordered between 12:00 and 14:00 costs £4.00
     */
    @Test
    public void discount2() {
        BigDecimal discountPrice = new BigDecimal("4.00");

        // Discount should automatically apply once added
        setupOrder("2021-03-07T12:15Z");
        assertEquals(discountPrice, testBasket.getTotalIncome());

        clearBasket();

        // Discount should not apply after 14:00
        setupOrder("2021-03-07T14:01Z");
        assertNotEquals(discountPrice, testBasket.getTotalIncome());

        clearBasket();

        // Discount should not apply before 12:00
        setupOrder("2021-03-07T11:59Z");
        assertNotEquals(discountPrice, testBasket.getTotalIncome());
    }

    /**
     * Tests that all food items are 50% off after 17:00
     */
    @Test
    public void discount3() {
        BigDecimal foodPrice = testMenu.getKey("F001").getPrice();
        BigDecimal drinkPrice = testMenu.getKey("B001").getPrice();

        // Food price is rounded before summation
        foodPrice = foodPrice.multiply(new BigDecimal("0.5"));
        foodPrice = foodPrice.setScale(2, RoundingMode.HALF_EVEN);

        // Find sum if food is 50% off (rounded half even as expected)
        BigDecimal discountPrice = drinkPrice.add(foodPrice);

        setupOrder("2021-03-07T18:15Z");
        assertEquals(discountPrice, testBasket.getTotalIncome());
    }
}
