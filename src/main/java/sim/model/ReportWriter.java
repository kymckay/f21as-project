package sim.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class responsible for creating and writing the end of day report from a list
 * of orders
 */
public class ReportWriter {
    StringBuilder report;

    /**
     * Consstruct a new report for the given list of customers
     * @param customers list of served customers to convert into a report
     */
    public ReportWriter(List<Customer> customers, Menu menu) {
        report = new StringBuilder();

        // Report should include today's date for future reference
        report.append(String.format("End of Day Report: %s%n", LocalDate.now()));
        
        for (String m : menu.keysSet()) {
            report.append(String.format("%-30s %s%n",
                menu.getItem(m).getName(),
                menu.getItem(m).getOrderCount()
            ));
        }

        // Loop through all the items in Menu to determine the item(s) with highest count
        int highestCount = 0;
        ArrayList<MenuItem> popular = new ArrayList<>();

        for (String m: menu.keysSet()) {
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

        if (getIncome(customers).signum() > 0) {
            report.append("\nThe most popular menu item(s) today: ");
            report.append(popular.stream().map(MenuItem::getName).collect(Collectors.joining(", ")));
            report.append(String.format(" ordered %d times", highestCount));
        } else {
            report.append("\nNo items were sold today");
        }

        int mostOrder = 0;
        ArrayList<Customer> mostOrderCust = new ArrayList<>();
        for (Customer c : customers) {
            int countOrder = c.getOrder().length;

            if (countOrder > mostOrder) {
                mostOrder = countOrder;
                mostOrderCust.clear();
                mostOrderCust.add(c);
            } else if (countOrder == mostOrder) {
                mostOrderCust.add(c);
            }
        }

        report.append(String.format("/nThe most ordering customer(s) is/are: "));
        for (Customer customer : mostOrderCust) {
            report.append(String.format("\n %s%n", customer.getName()));
            report.append(String.format(" with %d", mostOrder, "items %s%n",  Arrays.toString(customer.getOrder())));
        }
        

        // Summary statistics include the day's income
        report.append(String.format("\n The income obtained from today's orders is £%s%n", getIncome(customers)));
    }

    /**
     * Write out a report for the list of orders provided
     * @param out file to write report to
     */
    public void write(File out) {
        try (FileWriter orderWriter = new FileWriter(out)) {
            orderWriter.write(report.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getIncome(List<Customer> customers) {
        BigDecimal total = BigDecimal.ZERO;

        for (Customer c : customers) {
            for (MenuItem item : c.getOrder()) {
                total = total.add(item.getPrice());
            }
        }

        return total;
    }
}
