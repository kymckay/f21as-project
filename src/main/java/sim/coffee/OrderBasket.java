package sim.coffee;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderBasket extends OrderList {

    private OrderList orderList;
    private Menu menu;

    // private HashMap<String, Integer> count = new HashMap<>();

    // Instantiate Map to track order count
    OrderBasket(Menu m, OrderList o, List<Order> list) {
    	super(list);
        menu = m;
        orderList = o;
    }

    // Parses through basket list and checks for discount
    private void applyDiscount() {
        // Loops through the element - order in orderList
        for (Order o : this.orders) {
            //local Variables shared ∀ discount cases
            BigDecimal price = o.getFullPrice();
            char id = o.getItemDetails().charAt(0);
            char bevHot = o.getItemDetails().charAt(2);
            // Switch case for the 3 differnt discount rules based on the order hour
            switch (o.getTime().getHour()) {
                // For orders ordered at 8.00 a.m. to 10.59 a.m. (Excluding 11.00 a.m.)
                // Hence, order hours 8, 9, 10
                case 8: case 9: case 10:
                    if (id == 'M') {
                        // In cases that its a merch (no discount)
                        o.setPricePaid(price);
                    } else if (bevHot == 'F') {
                        // In cases that its a beverage but not hot (no discount)
                        o.setPricePaid(price);
                        // Other cases (with discount)
                    } else {
                        price = price.multiply(new BigDecimal(7));
                        price = price.divide(new BigDecimal(100), 3, RoundingMode.CEILING);
                        o.setPricePaid(price);
                    }
                    break;
                
                // For every food + drink combo, final price is 4.00
                case 12: case 13:
                    // Set Final Price
                    BigDecimal finalPrice = new BigDecimal(4);
                    

                    break;
                case 17: case 18:

                    break;
                default:
                    
                    break;
            }
        }
    }

    // dumps basket contents into supplied OrderList
    private boolean checkout() {
        return orderList.addAll(this.orders);
    }

    // adds value to the map
    @Override
    public boolean add(Order o) {
        applyDiscount();
        checkout();
        super.add(o);
        String a = o.getItemId();
        menu.getKey(a).setCount();
        // count.put(a, count.get(a) + 1);
        return true;
    }



}
