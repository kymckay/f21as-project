package sim.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Class responsible for creating and writing the end of day report from a list
 * of orders
 */
public class ReportWriter {
    StringBuilder report;

    /**
     * Consstruct a new report for the given list of orders
     * @param orders list of orders to convert into a report
     */
    public ReportWriter(List<Order[]> orders) {
        report = new StringBuilder();

        // Report should include today's date for future reference
        report.append(String.format("End of Day Report: %s%n", LocalDate.now()));

        // Summary statistics include the day's income
        report.append(String.format("The income obtained from today's orders is £%s%n", getIncome(orders)));
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

    private BigDecimal getIncome(List<Order[]> orders) {
        BigDecimal total = BigDecimal.ZERO;

        for (Order[] items : orders) {
            for (Order o : items) {
                // TODO reinstate
                total = total.add(BigDecimal.ZERO);
            }
        }

        return total;
    }
}
