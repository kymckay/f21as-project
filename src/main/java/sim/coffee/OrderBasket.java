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
    private void applyDiscount() {
        // for (int i = 0; i < this.orders.size(); i++) {
        //     list.get(i).getItemDetails().
        // }
        // Loops through the element - order in orderList
        for (Order o : this.orders) {
            
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
        String a = o.getItemDetails().getId();
        menu.getKey(a).setCount();
        // count.put(a, count.get(a) + 1);
        return true;
    }



}
