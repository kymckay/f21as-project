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
import java.util.stream.IntStream;

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
        // Create counter to keep track no. of each type of items ordered
        int[] count = {0, 0, 0}; // {'F', 'B', 'M'}
        for (Order o : this.orders) {
            switch (o.getItemDetails().charAt(0)) {
                case 'F':
                    count[0]++;
                    break;
                case 'B':
                    count[1]++;
                    break;
                default:
                    count[2]++;
                    break;
            }
        }
        // Get sum of count array
        int sumCount = IntStream.of(count).sum();
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
                // Only applies when count[0] = count[1] and count[0], count[1] > 0
                case 8: case 9: case 10:
                    if (sumCount > 0 && count[0]==count[1]) {
                        if (id == 'M') {
                            // In cases that its a merch (no discount)
                            o.setPricePaid(price);
                        } else if (bevHot == 'F' || bevHot == 'f') {
                            // In cases that its a beverage but not hot (no discount)
                            o.setPricePaid(price);
                            // Other cases (with discount)
                        } else {
                            price = price.multiply(new BigDecimal(7));
                            price = price.divide(new BigDecimal(100), 3, RoundingMode.CEILING);
                            o.setPricePaid(price);
                        }
                        break;
                    }
                
                // For every food + drink combo, final price is 4.00
                // For orders made between 12.00 p.m. to 13.59 p.m.
                case 12: case 13:
                    // Set Final Price
                    BigDecimal finalPrice = new BigDecimal(4);
                    

                    break;
                case 17: case 18:

                    break;

                default:
                    o.setPricePaid(price);
                    break;
            }
        }
    }

    // dumps basket contents into supplied OrderList
    public boolean checkout() {
        return orderList.addAll(this.orders);
    }

    // adds value to the map
    @Override
    public boolean add(Order o) {
        applyDiscount();
        super.add(o);
        String a = o.getItemId();
        menu.getKey(a).setCount();
        // count.put(a, count.get(a) + 1);
        return true;
    }



}
