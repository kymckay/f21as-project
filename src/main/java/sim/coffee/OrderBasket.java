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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        int[] count = {0, 0, 0, 0}; // {Sandwich, Pastry, 'B', 'M'}
        for (Order o : this.orders) {
            switch (o.getItemDetails().charAt(0)) {
                case 'F':
                    // Use Regex to track the food item and differentiate
                    // // between pastry and sandwich
                    String desc = menu.getKey(o.getItemId()).getDescription();
                    Pattern pattern = Pattern.compile("sandwich", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(desc);
                    boolean matchFound = matcher.find();
                    if (matchFound) {
                        count[0]++; // If matchFound = true, the sandwich count ++
                    } else {
                        count[1]++;
                    }
                    break;

                case 'B':
                    count[2]++;
                    break;

                default:
                    count[3]++;
                    break;
            }
        }
        // Get sum of count array
        int sumCount = IntStream.of(count).sum();

        // Order has to have more than 1 item
        if (sumCount > 0) {
            // Loops through the element - order in orderList
            for (Order o : this.orders) {

                // local Variables shared ∀ discount cases
                BigDecimal price = o.getFullPrice();
                char id = o.getItemDetails().charAt(0);
                char bevHot = o.getItemDetails().charAt(2);

                // Switch case for the 3 different discount rules based on the order hour
                switch (o.getTime().getHour()) {

                    // For orders ordered at 8.00 a.m. to 10.59 a.m. (Excluding 11.00 a.m.)
                    // Hence, order hours 8, 9, 10
                    // Only applies when count[0] = count[2], count[1] = count[2]
                    // // and count[0], count[1], count[2] > 0
                    case 8: case 9: case 10:
                        if (count[0] == count[2] && count[1] == count[2]) {
                            if (bevHot == 'F' || bevHot == 'f') {
                                // In cases that its a beverage but not hot (no discount)
                                o.setPricePaid(price);
                                
                            } else {
                                // Other cases (with discount)
                                price = price.multiply(new BigDecimal(7));
                                price = price.divide(new BigDecimal(100), 3, RoundingMode.CEILING);
                                o.setPricePaid(price);
                            }
                            break;

                        } else {
                            o.setPricePaid(price);
                        }
                    
                    // For every food + drink combo, final price is 4.00
                    // For orders made between 12.00 p.m. to 13.59 p.m.
                    case 12: case 13:
                        // Sets Meal Deal Price
                        BigDecimal finalPrice = new BigDecimal(4);
                        if (condition) {
                            
                        } else {
                            
                        }

                        break;
                    case 17: case 18:

                        break;

                    default:
                        o.setPricePaid(price);
                        break;
                }
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
