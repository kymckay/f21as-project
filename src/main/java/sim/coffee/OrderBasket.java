package sim.coffee;

import java.lang.reflect.Method;
import java.math.BigDecimal;
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
            switch (o.getTime().of(year, month, dayOfMonth, hour, minute, second)) {
                case value:
            
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
        String a = o.getItemDetails().getI;
        menu.getKey(a).setCount();
        // count.put(a, count.get(a) + 1);
        return true;
    }



}
