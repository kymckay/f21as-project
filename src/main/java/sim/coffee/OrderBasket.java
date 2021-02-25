package sim.coffee;

import java.util.List;

public abstract class OrderBasket extends OrderList {

    protected OrderList orderList;
    protected Menu menu;

    // private HashMap<String, Integer> count = new HashMap<>();

    // Instantiate Map to track order count
    OrderBasket(Menu m, OrderList o, List<Order> list) {
    	super(list);
        menu = m;
        orderList = o;
    }

    // Breakfast Deal – 30% off any hot drink + pastry ordered between 8:00 and 11:00   
    // Meal Deal – Any drink + sandwich + pastry ordered between 12:00 and 14:00 costs £4.00  
    // End-of-the-Day Deal – 50% off all pastries and sandwiches food ordered after 17:00 and before closing – 19:00 
    // Parses through basket list and checks for discount
    public void applyDiscount() {}

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
