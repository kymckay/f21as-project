package sim.coffee;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    // Breakfast Deal – 30% off any hot drink + pastry ordered between 8:00 and 11:00
    // Meal Deal – Any drink + sandwich + pastry ordered between 12:00 and 14:00 costs £4.00
    // End-of-the-Day Deal – 50% off all pastries and sandwiches food ordered after 17:00 and before closing – 19:00
    // Parses through basket list and checks for discount
    public boolean ifSandwich(Order o) {
        // Use Regex to track the food item and differentiate
        // // between pastry and sandwich
        String desc = menu.getKey(o.getItemId()).getDescription();
        Pattern pattern = Pattern.compile("Sandwich", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(desc);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    // Create counter to keep track no. of each type of items ordered
    public int[] getCount() {
        int[] count = {0, 0, 0, 0, 0}; // {Sandwich, Pastry, 'B–hot', 'B-cold', 'M'}
        for (Order o : this.orders) {
            switch (o.getItemId().charAt(0)) {
                case 'F':
                    if (ifSandwich(o)) {
                        count[0]++; // If ifSandwich = true, the sandwich count ++
                    } else {
                        count[1]++;
                    }
                    break;
                case 'B':
                    if (isHot(o)) {
                        count[2]++;
                    } else {
                        count[3]++;
                    }
                    break;
                default:
                    count[4]++;
                    break;
            }
        }
        return count;
    }

    // Get sum of counter array
    public int getSumCount() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            count = count + getCount()[i];
        }
        return count;
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

    // Applies only if order contains pastry [1] and hot drink [2]
    // Only applies when getCount[0] = getCount[2], getCount[1] = getCount[2]
    // // and getCount[0], getCount[1], getCount[2] > 0
    public void morningDiscount(Order o) {

        BigDecimal price = getBasePrice(o);

        if (getCount()[0] > 0 || getCount()[2] > 0) {

            if (ifSandwich(o) == false || !isHot(o) || getItemType(o) == 'M') {

                o.setPricePaid(price);

            } else {

                BigDecimal sandwich = new BigDecimal(getCount()[0]);
                BigDecimal hotBev = new BigDecimal(getCount()[2]);
                BigDecimal countD = hotBev.subtract(sandwich);
                BigDecimal discount = new BigDecimal(0.3);
                BigDecimal baseFactor = new BigDecimal(1);
                BigDecimal factor = new BigDecimal(1);

                if (sandwich.equals(hotBev)) {

                    factor = baseFactor.subtract(discount);
                    price = price.multiply(factor);
                    o.setPricePaid(price);

                } else if (countD.signum() < 0) {

                    if (getItemType(o) == 'F') {

                        BigDecimal num = countD.multiply(new BigDecimal(-1));
                        num = num.add(hotBev);
                        discount = discount.divide(num);
                        factor = baseFactor.add(discount);
                        price = price.multiply(factor);
                        o.setPricePaid(price);

                    } else {

                        factor = baseFactor.subtract(discount);
                        price = price.multiply(factor);
                        o.setPricePaid(price);

                    }

                } else {

                    if (getItemType(o) == 'B') {

                        BigDecimal num = countD.add(sandwich);
                        discount = discount.divide(num);
                        factor = baseFactor.add(discount);
                        price = price.multiply(factor);

                    } else {

                        factor = baseFactor.subtract(discount);
                        price = price.multiply(factor);
                        o.setPricePaid(price);

                    }

                }

            }

        } else {

            o.setPricePaid(price);

        }

    }

    // Figures out what discount may apply and attempts to
    public void applyDiscount() {

        // Nothing to do if the basket is empty, sanity check
        if (orders.isEmpty()) return;

        // First element in the list is when overall customer order started
        int hour = orders.get(0).getTime().getHour();

        // Different discount applies based on hour the order initiated
        if (hour >= 8 && hour < 11) {
            discount1();
        } else if (hour >= 12 && hour < 14) {
            discount2();
        } else if (hour >= 17) {
            discount3();
        }
    }

    // Applys discount 1 if basket qualifies
    // Discount 1 = 30% off hot drink and sandwich combos
    private void discount1(Order incomingOrder) {
        if (incomingOrder.getItemId().startsWith("M"))
            return;

        String target = incomingOrder.getItemId().startsWith("F") ? "B" : "F";

        Order saveOrder;

        for (Order order : orders) {
            if (order.hasDiscount()) continue;

            if (order.getItemId().startsWith(target)) {
                saveOrder = order;
                break;
            }
        }

        
    }

    // Applys discount 2 if basket qualifies
    // Discount 2 = food and drink combo is £4.00
    private void discount2() {
        // Track items as we go since list is unsorted
        ArrayList<Order> foodFound = new ArrayList<>();
        ArrayList<Order> drinkFound = new ArrayList<>();
        ArrayList<Order> merchFound = new ArrayList<>();
        BigDecimal mealDealPrice = new BigDecimal(4).setScale(2, RoundingMode.HALF_EVEN);

        for (Order order : orders) {
            // TODO build lists, skipping already discounted
            if (order.hasDiscount()) continue;

            if (order.getItemId().startsWith("F")) {
                foodFound.add(order);
            } else if (order.getItemId().startsWith("B")) {
                drinkFound.add(order);
            } else {
                merchFound.add(order);
            }
        }

        // Once we know all the food/drink orders, apply discounts
        BigDecimal singleItemPrice = mealDealPrice.divide(new BigDecimal(2)).setScale(2, RoundingMode.HALF_EVEN);
        int foodCount = foodFound.size();
        int drinkCount = drinkFound.size();

        if (foodCount == drinkCount) {
            for (int i = 0; i < foodCount; i++) {
                foodFound.get(i).setPricePaid(singleItemPrice);
                drinkFound.get(i).setPricePaid(singleItemPrice);
            }
        } else if (foodCount < drinkCount) {
            for (int i = 0; i < foodCount; i++) {
                foodFound.get(i).setPricePaid(singleItemPrice);
                drinkFound.get(i).setPricePaid(singleItemPrice);
            }
        } else {
            for (int i = 0; i < drinkCount; i++) {
                foodFound.get(i).setPricePaid(singleItemPrice);
                drinkFound.get(i).setPricePaid(singleItemPrice);
            }
        }

        orders.clear();
        orders.addAll(foodFound);
        orders.addAll(drinkFound);
        orders.addAll(merchFound);
        
    }

    // Applys discount 3 if basket qualifies
    // Discount 3 = food 50% off
    private void discount3() {
        for (Order order : orders) {
            if (order.getItemId().startsWith("F")) {
                // Skip over orders already discounted
                if (order.hasDiscount()) continue;

                BigDecimal price = order.getFullPrice();

                price = price.multiply(new BigDecimal("0.5"));
                price = price.setScale(2, RoundingMode.HALF_EVEN);

                order.setPricePaid(price);
            }
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
        applyDiscount();

        return added;
    }
    
  //writeReport Method
   	public void writeReport(String fileName)
       {
		// Increment count for menu items to reflect orders added (from order.csv)
		for (int i = 0; i < orderList.size(); i++) {
			String order = orderList.get(i).getItemId();
			for (String m: menu.keySet()) {
				String menuItem = menu.getKey(m).getID();
				if (menuItem.equals(order)) {
					menu.getKey(m).setCount();
				}
			}
		}	
       	
    	String report = "";
    	report += String.format("%-25s", "Menu Item");
       	report += "Times Ordered" + "\n";
  
       	for(String menuItemKey : menu.keySet())
       	{
       		
       		
       		report += String.format("%-30s", menu.getKey(menuItemKey).getDescription() + " " );
       		report += String.format("%s\n",menu.getKey(menuItemKey).getOrderCount());
       	    
        }
       	
       String message = "The Total Income obtained from the today's Orders is £";
       report += "\n" + message;
       report += orderList.getTotalIncome();
       
       // Loop through all the items in Menu to determine the item(s) with highest count
       int highestCount = 0;
       for(String m: menu.keySet()) {
    	   int count = menu.getKey(m).getOrderCount();
    	   if (highestCount < count) {
    		   highestCount = count;   
    	   }
       }
       // Add all menu items with a count equal to highest count to a string
       String mostPopularItem = "";
       for(String m: menu.keySet()) {
    	   int itemCount = menu.getKey(m).getOrderCount();
    	   if (highestCount == itemCount) {
    		   mostPopularItem += menu.getKey(m).getDescription();
    		   mostPopularItem += ", ";
    	   }
       }
       
       report += "\n" + "The most popular menu item(s) today: " + mostPopularItem + "ordered " + highestCount + " times";
       
       
       
       try {													//Writes the report and message to the file
			FileWriter orderWriter = new FileWriter(fileName);
			orderWriter.write(report);
			orderWriter.close();   					
		} catch(IOException e) {
			e.printStackTrace();
		}
   	}
}