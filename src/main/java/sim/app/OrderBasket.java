package sim.app;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import sim.coffee.Menu;
import sim.coffee.MenuItem;

public class OrderBasket extends OrderList {

    private OrderList orderList;
    private Menu menu;

    public OrderBasket(Menu m, OrderList o, List<Order> list) {
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

        // Item count should increment now that they're purchased
        for (Order o : orders) {
            menu.getItem(o.getItemId()).setCount();
        }

        orders.clear();
        return added;
    }

    // Adds an order to the basket and applies discounts
    @Override
    public boolean add(Order o) {
        boolean added = super.add(o);

        // Discounts should be updated each time an item is added
        applyDiscount(o);

        return added;
    }

   	public void writeReport(String fileName) {
        // Local Variable, parsing today into getTodayIncome() to prevent
        // the program from returning the sum of income from all the oders in the past
        LocalDate today = LocalDate.now();

        // Set a reasonable inital character capacity knowing 4 = length of an ID
    	StringBuilder report = new StringBuilder(menu.keySet().size() * 10);
    	report.append(String.format("%-25s", "Menu Item"));
       	report.append("Times Ordered\n");

       	for (String k : menu.keySet()) {
       		report.append(String.format("%-30s %s%n",
                menu.getItem(k).getDescription(),
                menu.getItem(k).getOrderCount()
            ));
        }

        // includes a report for the cummulative income up till this day – "today"
        report.append("\nThe cumulative income from all orders is £" + orderList.getTotalIncome());
        report.append("\nThe income obtained from today's orders is £" + orderList.getDayIncome(today));

        // Loop through all the items in Menu to determine the item(s) with highest count
        int highestCount = 0;
        ArrayList<MenuItem> popular = new ArrayList<>();

        for (String m: menu.keySet()) {
            MenuItem item = menu.getItem(m);
            int count = item.getOrderCount();

            // New highest count, reset the list
            if (highestCount < count) {
                highestCount = count;
                popular.clear();
            }

            // Find all items with highest count
            if (highestCount == count) {
                popular.add(item);
            }
        }

        if (orderList.getDayIncome(today).signum() > 0) {
            report.append("\nThe most popular menu item(s) today: ");
            report.append(popular.stream().map(MenuItem::getDescription).collect(Collectors.joining(", ")));
            report.append(String.format(" ordered %d times", highestCount));
        } else {
            report.append("\nNo items were sold today");
        }

        try (FileWriter orderWriter = new FileWriter(fileName)) {
			orderWriter.write(report.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
   	}
}