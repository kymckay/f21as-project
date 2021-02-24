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
    // TODO use setPricePaid
    private void applyDiscount() {
        // Loops through the element - order in orderList
        for (Order o : this.orders) {
            BigDecimal price = o.getFullPrice();
            char id = o.getItemDetails().charAt(0);
            switch (o.getTime().getHour()) {
                case 8: case 9: case 10:
                    if (id == 'F' && id == 'B') {
                        price = price.multiply(new BigDecimal(7));
                        price = price.divide(new BigDecimal(100), 3, RoundingMode.CEILING);
                        o.setPricePaid(price);
                    }
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
