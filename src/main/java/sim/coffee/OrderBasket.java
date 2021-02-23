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
        // Duplicate keys from Menu and replace
        // // new hashmap's keys with the same key
        // // for (String order : m.keySet()) {
        // //     count.put(order, 0);
        // // }
        menu = m;
        orderList = o;
    }

    public boolean updateCount(String id) {
        menu.getKey(id).setCount(id);
        return true;
    }
    // adds value to the map
    @Override
    public boolean add(Order o) {
        super.add(o);
        String a = o.getItemDetails().getId();
        updateCount(a);
        // count.put(a, count.get(a) + 1);
        return true;
    }

}    
