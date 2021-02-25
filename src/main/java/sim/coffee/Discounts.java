package sim.coffee;

import java.util.List;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Discounts extends OrderBasket {

    Discounts(Menu m, OrderList o, List<Order> list) {
        super(m, o, list);
    }

    public boolean ifSandwich(Order o) {
        // Use Regex to track the food item and differentiate
        // // between pastry and sandwich
        String desc = menu.getKey(o.getItemId()).getDescription();
        Pattern pattern = Pattern.compile("sandwich", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(desc);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    // Create counter to keep track no. of each type of items ordered
    public int getCount(int i) {
        int[] count = {0, 0, 0, 0, 0}; // {Sandwich, Pastry, 'B–hot', 'B-cold', 'M'}
        for (Order o : this.orders) {
            switch (o.getItemDetails().charAt(0)) {
                case 'F':
                    if (ifSandwich(o)) {
                        count[0]++; // If ifSandwich = true, the sandwich count ++
                    } else {
                        count[1]++;
                    }
                    break;
                case 'B':
                    char bevHot = o.getItemDetails().charAt(2);
                    if (bevHot == 't') {
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
        return count[i];
    }

    // Get sum of counter array
    public int getSumCount() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            count = count + getCount(i);
        }
        return count;
    }

    public BigDecimal getBasePrice(Order o) {
        return o.getFullPrice();
    }

    public Character isHot(Order o) {
        return o.getItemDetails().charAt(2);
    }

    public Character getItemType(Order o) {
        return o.getItemId().charAt(0);
    }

    // Applies only if order contains pastry [1] and hot drink [2]
    // Only applies when getCount[0] = getCount[2], getCount[1] = getCount[2]
    // // and getCount[0], getCount[1], getCount[2] > 0
    public void morningDiscount(Order o) {

        BigDecimal price = getBasePrice(o);

        if (getCount(1) >= 1 && getCount(2) >= 1) {

            if (ifSandwich(o) || isHot(o) == 'f' || getItemType(o) == 'M') {
                
                o.setPricePaid(price);

            } else {

                BigDecimal pastry = new BigDecimal(getCount(1));
                BigDecimal hotBev = new BigDecimal(getCount(2));
                BigDecimal countD = hotBev.subtract(pastry);
                BigDecimal discount = new BigDecimal(0.3);
                BigDecimal baseFactor = new BigDecimal(1);
                BigDecimal factor = new BigDecimal(1);
                
                if (countD.equals(BigDecimal.ZERO)) {

                    baseFactor = baseFactor.subtract(discount);
                    price = price.multiply(factor);
                    o.setPricePaid(price);

                } else if (countD.signum() < 0) {

                    if (ifSandwich(o)) {

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
                        
                        BigDecimal num = countD.add(pastry);
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
        int foodInt = getCount(0) + getCount(1);
        int bevInt = getCount(2) + getCount(3);


        if (foodInt >= 1 && bevInt >= 1) {

            BigDecimal food = new BigDecimal(foodInt);
            BigDecimal bev = new BigDecimal(bevInt);
            BigDecimal countD = bev.subtract(food);
            // £4/2 = £2
            BigDecimal dealPrice = new BigDecimal(2);

            if (countD.equals(BigDecimal.ZERO)) {

                o.setPricePaid(dealPrice);
                
            } else if (countD.signum() < 0) {

                if (getItemType(o) == 'f') {

                    countD = countD.multiply(new BigDecimal(-1));
                    price = countD.multiply(price);
                    price = price.add(bev.multiply(dealPrice));
                    price = price.divide(food);
                    o.setPricePaid(price);
                    
                } else {

                    o.setPricePaid(dealPrice);
                    
                }
                
            } else {

                if (getItemType(o) == 'B') {

                    price = countD.multiply(price);
                    price = price.add(food.multiply(dealPrice));
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

        if (getCount(0) > 0 || getCount(1) > 0) {

            if (getItemType(o) == 'F') {

                price = price.multiply(factor);
                o.setPricePaid(price);
                
            }
            
        } else {

            o.setPricePaid(price);
            
        }

    }

    @Override
    public void applyDiscount() {

        // Order has to have more than 1 item
        if (getSumCount() > 0) {

            // Loops through the element - order in orderList
            for (Order o : this.orders) {

                // Switch case for the 3 different discount rules based on the order hour
                switch (o.getTime().getHour()) {

                    // For orders ordered at 8.00 a.m. to 10.59 a.m. (Excluding 11.00 a.m.)
                    // Hence, order hours 8, 9, 10
                    case 8: case 9: case 10:
                        morningDiscount(o);
                        break;
                    // For orders made between 12.00 p.m. to 1.59 p.m.
                    case 12: case 13:
                        afternoonDiscount(o);
                        break;
                    // For orders made between 5.00 p.m. to 6.59 p.m.
                    case 17: case 18:
                        eveningDiscount(o);
                        break;
                    default:
                        o.setPricePaid(getBasePrice(o));
                        break;

                }
            }
        }
    }
}
