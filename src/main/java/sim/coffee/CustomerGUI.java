package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

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
        // Table element will list menu items available to add to cart
        JTable menuTable = new JTable(menu);

        // Staff can only add one menu item at a time as their properties differ
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Menu item controls differ per item, updated when list selection changes
        menuTable.getSelectionModel().addListSelectionListener(this::updateMenuControls);

        // Row sorter is used to apply filters to the table
        TableRowSorter<MenuTableModel> sorter = new TableRowSorter<>(menu);
        menuTable.setRowSorter(sorter);

        // Scroll pane contains table to enable scrollbar
        JScrollPane menuPane = new JScrollPane();
        menuPane.setViewportView(menuTable);

        // Section with menu table
        guiFrame.add(menuPane, BorderLayout.CENTER);

        // Menu categories switched via panel of buttons at top of UI
        JPanel buttonPanel = new JPanel();
        JButton foodButton = new JButton("Food");
        JButton drinkButton = new JButton("Beverages");
        JButton merchButton = new JButton("Merchandise");

        // Buttons filter menu by item type
        foodButton.addActionListener(e -> filterMenu(sorter, "F"));
        drinkButton.addActionListener(e -> filterMenu(sorter, "B"));
        merchButton.addActionListener(e -> filterMenu(sorter, "M"));

        buttonPanel.add(foodButton);
        buttonPanel.add(drinkButton);
        buttonPanel.add(merchButton);
        guiFrame.add(buttonPanel, BorderLayout.NORTH); // Section with category buttons
    }

    private void setupCheckout() {
        // Table element will list menu items available to add to cart
        JTable ordersTable = new JTable(basket);

        // Scroll pane contains table to enable scrollbar
        JScrollPane ordersPane = new JScrollPane();
        ordersPane.setViewportView(ordersTable);

        guiFrame.add(ordersPane, BorderLayout.SOUTH); // Section with checkout orders table
    }

    private void updateMenuControls(ListSelectionEvent e) {
        // Don't want this event to fire fully if user click + drags
        // Would result in rapid UI changes
        if (!e.getValueIsAdjusting()) {
            // TODO: Update menu item related controls here
        }
    }

    private void filterMenu(TableRowSorter<MenuTableModel> sorter, String idPrefix) {
        // Categories can be differentiated by leading character of item ID (column 0)
        sorter.setRowFilter(RowFilter.regexFilter("^" + idPrefix, 0));
    }
}