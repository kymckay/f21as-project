package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import javax.swing.*;
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
    private BasketTableModel basket;

    // There's probably a better way to manage this but for now I'm just storing all relevant controls
    JComboBox<Colour> colours = new JComboBox<>();
    JComboBox<Label> labels = new JComboBox<>();
    JCheckBox isHot = new JCheckBox();
    JComboBox<Size> sizes = new JComboBox<>();
    JComboBox<Milk> milks = new JComboBox<>();

    // The current customer placing orders is tracked here
    private Random custRand = new Random();
    private String customer = nextCustomer();

    // State of the UI tracked here for ease of checking elsewhere
    private String menuView = LABEL_F;

    // Currently selected menu item for ease of access elsewhere
    private MenuItem selectedItem;

    CustomerGUI(MenuTableModel menu, BasketTableModel basket) {
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

    private JPanel merchandiseControls() {
        JPanel panel = new JPanel();

        JPanel controls = new JPanel();

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

    private JPanel beverageControls() {
        JPanel panel = new JPanel();

        JPanel controls = new JPanel();

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

        JButton add = new JButton("Add to Cart");
        JButton checkout = new JButton("Checkout");

        add.addActionListener(this::addToCart);
        checkout.addActionListener(this::onCheckout);

        panel.add(add);
        panel.add(checkout);

        return panel;
    }

    // Instantiates a new order from item controls and puts it in the basket
    private void addToCart(ActionEvent e) {
        OrderItem newItem;

        String itemId = selectedItem.getID();
        BigDecimal price = selectedItem.getPrice();
        String details = "";

        // Curent view dictates which item controls are relevant to generate item
        // details
        switch (menuView) {
            case LABEL_B:
                Size s = (Size)sizes.getSelectedItem();
                boolean hot = isHot.isSelected();
                Milk m = (Milk)milks.getSelectedItem();

                details = Beverage.formatDetails(s, hot, m);
                break;
            case LABEL_M:
                Label l = (Label) labels.getSelectedItem();
                Colour c = (Colour) colours.getSelectedItem();

                details = Merchandise.formatDetails(l, c);
                break;
            default:
                // Simple types (like food) have no specific details
                break;
        }

        newItem = new OrderItem(itemId, details, price, price);
        Order newOrder = new Order(LocalDateTime.now(), customer, newItem);

        basket.add(newOrder);
    }

    // Tells the basket to log all orders in the order history
    private void onCheckout(ActionEvent e) {
        basket.checkout();
        customer = nextCustomer();
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
     */
    private void updateMenuControls(ListSelectionEvent e) {
        // Don't want this event to fire fully if user click + drags
        // Would result in rapid UI changes
        if (!e.getValueIsAdjusting()) {
            // Single selection mode means there's only ever one index
            int i =  e.getFirstIndex();

            // Index is in terms of the view, does not correspond to underlying data
            i = menuTable.convertRowIndexToModel(i);

            selectedItem = menu.getRowItem(i);

            // Note there is probably a nicer way to do this
            // It feels very ugly, but it works for now
            if (selectedItem instanceof Food) {
                // TODO: Show dietary classes
            } else if (selectedItem instanceof Beverage) {
                sizes.removeAllItems();
                for (Size s : selectedItem.getSizes()) {
                    sizes.addItem(s);
                }

                milks.removeAllItems();
                for (Milk m : selectedItem.getMilks()) {
                    milks.addItem(m);
                }

                isHot.setEnabled(selectedItem.canBeHot());

                sizes.setSelectedIndex(0);
                milks.setSelectedIndex(0);
            } else {
                colours.removeAllItems();
                for (Colour c : selectedItem.getColours()) {
                    colours.addItem(c);
                }

                labels.removeAllItems();
                for (Label l : selectedItem.getLabels()) {
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

        // Track state of menu for ease of checking elsewhere
        menuView = label;
    }

    // Generates a random customer ID to represent next customer in queue
    private String nextCustomer() {
        char c1 = (char) (custRand.nextInt(26) + 'a');
        char c2 = (char) (custRand.nextInt(26) + 'a');

        int d1 = custRand.nextInt(10);
        int d2 = custRand.nextInt(10);
        int d3 = custRand.nextInt(10);

        return String.format("%c%c%d%d%d", c1, c2, d1, d2, d3);
    }
}