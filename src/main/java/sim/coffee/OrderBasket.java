package sim.coffee;

import java.util.HashMap;

public class OrderBasket extends OrderList{

    private static Menu menu;
    private static OrderList order;

    static HashMap<Menu, Integer> count = new HashMap<>();

    OrderBasket(Menu m, OrderList o) {
        menu = m;
        order = o;
    }

    public static void orderCount() {
        for (Menu i : count.keySet()) {
            
        }
    }
}    
