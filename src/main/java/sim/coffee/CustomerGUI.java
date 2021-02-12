package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CustomerGUI {
    private JFrame guiFrame;

    private MenuTableModel menu;
    private OrderList processed;
    private OrderTableModel basket;

    CustomerGUI(MenuTableModel menu, OrderList processed, OrderTableModel basket) {
        this.menu = menu;
        this.processed = processed;
        this.basket = basket;

        guiFrame = new JFrame("Order Processing");

        // Prevent making the window too small to use
        guiFrame.setMinimumSize(new Dimension(500, 500));

        guiFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setupMenu();
        setupCheckout();

        // Let frame layout manager handle sizing
        guiFrame.pack();
        guiFrame.setVisible(true);
    }

    private void setupMenu() {
        // Menu categories switched via panel of buttons at top of UI
        JPanel buttonPanel = new JPanel();

        // Scroll pane contains table to enable scrollbar
        JScrollPane menuPane = new JScrollPane();

        guiFrame.add(buttonPanel, BorderLayout.NORTH); // Section with category buttons
        guiFrame.add(menuPane, BorderLayout.CENTER); // Section with menu table
    }

    private void setupCheckout() {
        // Scroll pane contains table to enable scrollbar
        JScrollPane ordersPane = new JScrollPane();

        guiFrame.add(ordersPane, BorderLayout.SOUTH); // Section with checkout orders table
    }
}