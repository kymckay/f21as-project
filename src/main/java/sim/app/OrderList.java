package sim.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderList {

	List<Order> orders;

	public OrderList(List<Order> list) { // constructor accepts both LinkedList and ArrayList instances
		orders = list;
	}

	// Simple wrapper method
	public boolean add(Order o) {
		return orders.add(o);
	}

	// dumps basket to OrderList
	public boolean addAll(List<Order> l) {
		return orders.addAll(l);
	}

	// Simple wrapper method
	public Order get(int index) {
		return orders.get(index);
	}

	// general getter method for income
	public BigDecimal getTotalIncome() {

		// create variable to be returned after looping through orders
		BigDecimal sum = new BigDecimal("0");

		// Loop through all the orders
		for (Order o : orders) {
			sum = sum.add(o.getPricePaid());
		}

		// No rounding needed as prices already rounded
		return sum;
	}

	/**
	 * Income method for the day,
	 * @return cummulative income for orders received for a specific day
	 */
	public BigDecimal getDayIncome(LocalDate date) {

		// create variable to be returned after looping through orders
		BigDecimal sum = new BigDecimal("0");

		// Loop through all the orders
		for (Order o : orders) {
			// only add the income for orders that are
			// from the same day the report is being created
			if (o.getDate().equals(date)) {
				sum = sum.add(o.getPricePaid());
			}
		}

		// No rounding needed as prices already rounded
		return sum;

	}

	public int size() {
		return orders.size();
	}
}
