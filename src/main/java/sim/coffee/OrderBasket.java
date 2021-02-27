package sim.coffee;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public void afternoonDiscount(Order o) {

        BigDecimal price = getBasePrice(o);
        int foodInt = getCount()[0] + getCount()[1];
        int bevInt = getCount()[2] + getCount()[3];


        if (foodInt >= 1 && bevInt >= 1) {

            BigDecimal food = new BigDecimal(foodInt);
            BigDecimal bev = new BigDecimal(bevInt);
            BigDecimal countD = bev.subtract(food);
            // £4/2 = £2
            BigDecimal dealPrice = new BigDecimal(2);

            if (countD.equals(BigDecimal.ZERO)) {

                o.setPricePaid(dealPrice);

            } else if (countD.signum() < 0) {

                if (getItemType(o) == 'F') {

                    countD = countD.multiply(new BigDecimal(-1));
                    price = countD.multiply(price);
                    price = price.add(food.multiply(dealPrice));
                    price = price.divide(food);
                    o.setPricePaid(price.setScale(2, RoundingMode.HALF_EVEN));

                } else {

                    o.setPricePaid(dealPrice.setScale(2));

                }

            } else {

                if (getItemType(o) == 'B') {

                    price = countD.multiply(price);
                    price = price.add(bev.multiply(dealPrice));
                    price = price.divide(bev);
                    o.setPricePaid(price);

                } else {

                    o.setPricePaid(price);

                }

            }

        } else {

            o.setPricePaid(price);

        }

    }

    public void eveningDiscount(Order o) {

        BigDecimal price = getBasePrice(o);
        BigDecimal factor = new BigDecimal(0.5);

        if (getCount()[0] > 0 || getCount()[1] > 0) {

            if (getItemType(o) == 'F') {

                price = price.multiply(factor);
                o.setPricePaid(price);

            }

        } else {

            o.setPricePaid(price);

        }

    }

    public void applyDiscount() {

        // Nothing to do if the basket is empty, sanity check
        if (orders.isEmpty()) return;

        // First element in the list is when overall customer order started
        int hour = orders.get(0).getTime().getHour();

        // Different discount applies based on hour the order initiated
        if (hour >= 8 && hour < 11) {
            // TODO: Morning discount
        } else if (hour >= 12 && hour < 14) {
            // TODO: Afternoon discount
        } else if (hour >= 17) {
            // TODO: Evening discount
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

        // Item tally can be incremented here (unless a remove from cart option is
        // added)
        menu.getKey(o.getItemId()).setCount();

        return added;
    }
}