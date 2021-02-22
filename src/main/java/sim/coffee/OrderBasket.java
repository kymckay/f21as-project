package sim.coffee;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderBasket extends OrderList {

    private OrderList orderList;

    private HashMap<String, Integer> count = new HashMap<>();

    // Instantiate Map to track order count
    OrderBasket(Menu m, OrderList o) {
        // Duplicate keys from Menu and replace
        // new hashmap's keys with the same key
            // int i = -1;
            // while (i < m.keySet().size()) {
            //     i++;
            //     String key = String.valueOf(m.getKey(String.valueOf(i)));
            //     int value = 0;
            //     count.put(key, value);
            // }
        for (String order : m.keySet()) {
            count.put(order, 0);
        }
        orderList = o;
    }

    // adds value to the map
    @Override
    public boolean add(Order o) {
        super.add(o);
        String a = o.getItemDetails().getId();
        count.put(a, count.get(a) + 1);
        return true;
    }

    // public static void orderCount() {
    //     for (Menu i : count.keySet()) {
    //         if (order.equals(menu.getKey(key))) {
    //         //If 
    //         } else {
                
    //         }
    //     }
    // }

}    
