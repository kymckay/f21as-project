package sim.coffee;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderBasket extends OrderList {

    private OrderList orderList;
    private Menu menu;

    // private HashMap<String, Integer> count = new HashMap<>();

    // Instantiate Map to track order count
    OrderBasket(Menu m, OrderList o) {
        menu = m;
        orderList = o;
    }

    // adds value to the map
    @Override
    public boolean add(Order o) {
        super.add(o);
        String a = o.getItemDetails().getId();
        menu.getKey(a).setCount();
        // count.put(a, count.get(a) + 1);
        return true;
    }

}    
