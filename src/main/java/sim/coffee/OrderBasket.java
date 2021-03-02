package sim.coffee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderBasket extends OrderList {

    private OrderList orderList;
    private Menu menu;

    OrderBasket(Menu m, OrderList o, List<Order> list) {
    	super(list);
        menu = m;
        orderList = o;
    }

    // Method to check if order is a sandwich, 
    // Only used for the breakfast deal - discount1()
    public boolean ifSandwich(Order o) {
        // Use Regex to track the food item and return a boolean - true, if item is a sandwich
        String desc = menu.getItem(o.getItemId()).getDescription();
        // match to see if String desc contains the string "Sandwich"
        Pattern pattern = Pattern.compile("Sandwich", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(desc);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    public BigDecimal getBasePrice(Order o) {
        return o.getFullPrice();
    }

    public boolean isHot(Order o) {
        return o.getItemDetails().split("\\|")[1].charAt(0) == 't';
    }

    public Character getItemType(Order o) {
        return o.getItemId().charAt(0);
    }

    // Figures out what discount may apply and attempts to
    public void applyDiscount(Order newOrder) {

        // Nothing to do if the basket is empty, sanity check
        if (orders.isEmpty()) return;

        // First element in the list is when overall customer order started
        int hour = newOrder.getTime().getHour();

        // Different discount applies based on hour the order initiated
        if (hour >= 8 && hour < 11) {
            discount1(newOrder);
        } else if (hour >= 12 && hour < 14) {
            discount2(newOrder);
        } else if (hour >= 17) {
            discount3(newOrder);
        }
    }

    // Applies discount 1 if basket qualifies
    // Discount 1 = 30% off hot drink and sandwich combos
    private void discount1(Order newOrder) {
        // Merchandise never applies for this discount
        if (newOrder.getItemId().startsWith("M"))
            return;

        // Food/beverage combines with opposite for discount
        String target = newOrder.getItemId().startsWith("F") ? "B" : "F";
        Order combo = null;

        for (Order order : orders) {
            if (
                !order.hasDiscount() // Already part of a combo
                && order.getItemId().startsWith(target)
                && (
                    (target.equals("B") && isHot(order)) // Beverage must be hot
                    || (target.equals("F") && ifSandwich(order)) // Food must be a sandwich
                )
            ) {
                combo = order;
                break;
            }
        }

        // If a suitable combo is found discount applies
        if (combo != null) {
            BigDecimal factor = new BigDecimal("0.7");

            BigDecimal price = combo.getFullPrice().multiply(factor).setScale(2, RoundingMode.HALF_EVEN);
            combo.setPricePaid(price);

            price = newOrder.getFullPrice().multiply(factor).setScale(2, RoundingMode.HALF_EVEN);
            newOrder.setPricePaid(price);
        }
    }

    // Applies discount 2 if basket qualifies
    // Discount 2 = food and drink combo is £4.00
    private void discount2(Order newOrder) {
        // Merchandise never applies for this discount
        if (newOrder.getItemId().startsWith("M")) return;

        // Food/beverage combines with opposite for discount
        String target = newOrder.getItemId().startsWith("F") ? "B" : "F";
        Order combo = null;

        for (Order order : orders) {
            if (
                !order.hasDiscount() // Already part of a combo
                && order.getItemId().startsWith(target)
            ) {
                combo = order;
                break;
            }
        }

        // If a suitable combo is found discount applies
        if (combo != null) {
            BigDecimal newPrice = new BigDecimal("2.00");
            combo.setPricePaid(newPrice);
            newOrder.setPricePaid(newPrice);
        }
    }

    // Applies discount 3 if basket qualifies
    // Discount 3 = food 50% off
    private void discount3(Order newOrder) {
        if (newOrder.getItemId().startsWith("F")) {
            BigDecimal price = newOrder.getFullPrice();

            price = price.multiply(new BigDecimal("0.5"));
            price = price.setScale(2, RoundingMode.HALF_EVEN);

            newOrder.setPricePaid(price);
        }
    }

    // Dumps basket contents into permanent order list and emptys the basket
    public boolean checkout() {
        boolean added = orderList.addAll(this.orders);
        orders.clear();
        return added;
    }

    // Adds an order to the basket and applies discounts
    @Override
    public boolean add(Order o) {
        boolean added = super.add(o);

        // Discounts should be updated each time an item is added
        applyDiscount(o);

        // Item count should increment (move to checkout if we enable "remove from basket")
        menu.getItem(o.getItemId()).setCount();

        return added;
    }

   	public void writeReport(String fileName) {
        // Local Variable, parsing today into getTodayIncome() to prevent
        // the program from returning the sum of income from all the oders in the past
        LocalDate today = LocalDate.now();

    	String report = "";
    	report += String.format("%-25s", "Menu Item");
       	report += "Times Ordered" + "\n";

       	for(String menuItemKey : menu.keySet())
       	{


       		report += String.format("%-30s", menu.getItem(menuItemKey).getDescription() + " " );
       		report += String.format("%s\n",menu.getItem(menuItemKey).getOrderCount());

        }

        // includes a report for the cummulative income up till this day – "today"
        report += "\n" + "The cumulative income from all orders are " + orderList.getTotalIncome();

        String message = "The Total Income obtained from the today's Orders is £";
        report += "\n" + message;
        report += orderList.getDayIncome(today);

        // Loop through all the items in Menu to determine the item(s) with highest count
        int highestCount = 0;
        for(String m: menu.keySet()) {
            int count = menu.getItem(m).getOrderCount();
            if (highestCount < count) {
                highestCount = count;
            }
        }
        // Add all menu items with a count equal to highest count to a string
        String mostPopularItem = "";
        for(String m: menu.keySet()) {
            int itemCount = menu.getItem(m).getOrderCount();
            if (highestCount == itemCount) {
                mostPopularItem += menu.getItem(m).getDescription();
                mostPopularItem += ", ";
            }
        }

        if (orderList.getDayIncome(today).signum() > 0) {
            report += "\n" + "The most popular menu item(s) today: " 
            + mostPopularItem + "ordered " + highestCount + " times";
        } else {
            report += "\n" + "No items are being sold today, don't give up, try again tomorrow! :)";
        }

        try {													//Writes the report and message to the file
			FileWriter orderWriter = new FileWriter(fileName);
			orderWriter.write(report);
			orderWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
   	}
}