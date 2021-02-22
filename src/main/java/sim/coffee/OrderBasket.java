package sim.coffee;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderBasket extends OrderList {

    private OrderList orderList;

    private HashMap<MenuItem, Integer> count = new HashMap<>();

    // Instantiate Map to track order count
    OrderBasket(Menu m, OrderList o) {
        // Duplicate keys from Menu and replace
        // new hashmap's keys with the same key
        int i = -1;
        while (i < m.menuMap.size()) {
            i++;
            MenuItem key = m.getKey(String.valueOf(i));
            int value = 0;
            count.put(key, value);
        }
        orderList = o;
    }

    // adds value to the map
    @Override
    public boolean add(Order o) {
        if (condition) {
            super.add(o);
            MenuItem a = o.getItemDetails().getId();
            // int i = count.get(a);
            // i++;
            count.put(a, count.get(a) + 1);
            a = directaccess //key to access hashmap
        } else {
            return false
        }

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
