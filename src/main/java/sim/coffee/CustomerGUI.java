package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

public class CustomerGUI {
    // Strings shown on buttons also correspond to control panel cards
    private static final String LABEL_F = "Food";
    private static final String LABEL_B = "Beverage";
    private static final String LABEL_M = "Merchandise";

    private JFrame guiFrame;

    // Table is needed after creation to convert row indices to model
    private JTable menuTable;

    // Menu table will be filterable by category using an applied row sorter
    private TableRowSorter<MenuTableModel> menuSorter;

    // Control panel needs to be updated in various places
    private JPanel controlPanel;

    private MenuTableModel menu;
    private OrderTableModel basket;

    // There's probably a better way to manage this but for now I'm just storing all relevant controls
    JComboBox<Colour> colours;
    JComboBox<Label> labels;
    JCheckBox isHot;
    JComboBox<Size> sizes;
    JComboBox<Milk> milks;

    CustomerGUI(MenuTableModel menu, OrderTableModel basket) {
        this.menu = menu;
        this.basket = basket;

        // Row sorter requires the table model to function correctly
        menuSorter = new TableRowSorter<>(menu);

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
        menuTable = new JTable(menu);

        // Staff can only add one menu item at a time as their properties differ
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Menu item controls differ per item, updated when list selection changes
        menuTable.getSelectionModel().addListSelectionListener(this::updateMenuControls);

        // Row sorter is used to apply filters to the table
        menuTable.setRowSorter(menuSorter);

        // Scroll pane contains table to enable scrollbar
        JScrollPane menuPane = new JScrollPane();
        menuPane.setViewportView(menuTable);

        // Section with menu table
        guiFrame.add(menuPane, BorderLayout.CENTER);

        // Item controls appear on the right of the menu
        controlPanel = new JPanel(new CardLayout());

        controlPanel.add(foodControls(), LABEL_F);
        controlPanel.add(beverageControls(), LABEL_B);
        controlPanel.add(merchandiseControls(), LABEL_M);

        // Menu starts on food by default (always filtered to one type at a time)
        filterMenu(LABEL_F);

        guiFrame.add(controlPanel, BorderLayout.EAST);

        // Menu categories switched via panel of buttons at top of UI
        JPanel buttonPanel = new JPanel();
        JButton foodButton = new JButton(LABEL_F);
        JButton drinkButton = new JButton(LABEL_B);
        JButton merchButton = new JButton(LABEL_M);

        // Buttons filter menu by item type
        foodButton.addActionListener(e -> filterMenu(LABEL_F));
        drinkButton.addActionListener(e -> filterMenu(LABEL_B));
        merchButton.addActionListener(e -> filterMenu(LABEL_M));

        buttonPanel.add(foodButton);
        buttonPanel.add(drinkButton);
        buttonPanel.add(merchButton);
        guiFrame.add(buttonPanel, BorderLayout.NORTH); // Section with category buttons
    }

    // TODO: Implement merch controls
    private JPanel merchandiseControls() {
        JPanel panel = new JPanel();

        JPanel controls = new JPanel();

        colours = new JComboBox<>();
        labels = new JComboBox<>();

        controls.add(colours);
        controls.add(labels);

        JPanel submit = cartControls();

        // Position checkout below item controls
        controls.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(controls);
        panel.add(submit);

        return panel;
    }

    // TODO: Implement beverage controls
    private JPanel beverageControls() {
        JPanel panel = new JPanel();

        JPanel controls = new JPanel();

        isHot = new JCheckBox();
        sizes = new JComboBox<>();
        milks = new JComboBox<>();

        controls.add(isHot);
        controls.add(sizes);
        controls.add(milks);

        JPanel submit = cartControls();

        // Position checkout below item controls
        controls.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(controls);
        panel.add(submit);

        return panel;
    }

    private JPanel foodControls() {
        JPanel panel = new JPanel();

        // TODO show dietary classes
        JLabel controls = new JLabel("No food controls to show.");
        JPanel submit = cartControls();

        // Position checkout below item controls
        controls.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(controls);
        panel.add(submit);

        return panel;
    }

    private JPanel cartControls() {
        JPanel panel = new JPanel();

        JButton checkout = new JButton("Add to Cart");

        checkout.addActionListener(this::onCheckout);

        panel.add(checkout);

        return panel;
    }

    private void onCheckout(ActionEvent e) {
        // TODO: Tell basket to checkout
    };

    private void setupCheckout() {
        // Table element will list menu items available to add to cart
        JTable ordersTable = new JTable(basket);

        // Scroll pane contains table to enable scrollbar
        JScrollPane ordersPane = new JScrollPane();
        ordersPane.setViewportView(ordersTable);

        guiFrame.add(ordersPane, BorderLayout.SOUTH); // Section with checkout orders table
    }

    /**
     * When the menu selection changes the item controls must be populated/disabled
     * to match the item's configuration
     *
     * @param e
     */
    private void updateMenuControls(ListSelectionEvent e) {
        // Don't want this event to fire fully if user click + drags
        // Would result in rapid UI changes
        if (!e.getValueIsAdjusting()) {
            // Single selection mode means there's only ever one index
            int i =  e.getFirstIndex();

            // Index is in terms of the view, does not correspond to underlying data
            i = menuTable.convertRowIndexToModel(i);

            MenuItem selected = menu.getRowItem(i);

            // Note there is probably a nicer way to do this
            // It feels very ugly, but it works for now
            if (selected instanceof Food) {
                // TODO: Show dietary classes
            } else if (selected instanceof Beverage) {
                sizes.removeAllItems();
                for (Size s : selected.getSizes()) {
                    sizes.addItem(s);
                }

                milks.removeAllItems();
                for (Milk m : selected.getMilks()) {
                    milks.addItem(m);
                }

                isHot.setEnabled(selected.canBeHot());

                sizes.setSelectedIndex(0);
                milks.setSelectedIndex(0);
            } else {
                colours.removeAllItems();
                for (Colour c : selected.getColours()) {
                    colours.addItem(c);
                }

                labels.removeAllItems();
                for (Label l : selected.getLabels()) {
                    labels.addItem(l);
                }
            }
        }
    }

    private void filterMenu(String label) {
        String idPrefix = label.substring(0, 1);

        // Categories can be differentiated by leading character of item ID (column 0)
        menuSorter.setRowFilter(RowFilter.regexFilter("^" + idPrefix, 0));

        // When menu is filtered corresponding controls are available
        CardLayout cl = (CardLayout) controlPanel.getLayout();
        cl.show(controlPanel, label);
    }
}