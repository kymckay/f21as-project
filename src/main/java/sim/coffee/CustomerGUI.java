package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
    private BasketTableModel basket;

    // The current customer placing orders is tracked here
    private Random custRand = new Random();
    private String customer = nextCustomer();

    // State of the UI tracked here for ease of checking elsewhere
    private String menuView = LABEL_F;

    // Currently selected menu item for ease of access elsewhere
    private MenuItem selectedItem;

    // Controls interact with the item properties
    ControlsBeverage controlsB = new ControlsBeverage();
    ControlsMerchandise controlsM = new ControlsMerchandise();

    CustomerGUI(MenuTableModel menu, BasketTableModel basket) {
        this.menu = menu;
        this.basket = basket;

        // Row sorter requires the table model to function correctly
        menuSorter = new TableRowSorter<>(menu);

        guiFrame = new JFrame("Order Processing");

        // Prevent making the window too small to use
        guiFrame.setMinimumSize(new Dimension(500, 500));

        guiFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Construct the layout and elements in order
        setup();

        // Let frame layout manager handle sizing
        guiFrame.pack();
        guiFrame.setVisible(true);
    }

    private void setup() {
        // Right of UI contains item and checkout controls stacked
        // Controls set before menu so it can start filtered
        JPanel sidebar = setupControls();
        guiFrame.add(sidebar, BorderLayout.EAST);

        // Center of UI has menu and basket tables stacked
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(setupMenu());
        centerPanel.add(setupBasket());
        guiFrame.add(centerPanel, BorderLayout.CENTER);

        // Top of UI contains menu category buttons
        guiFrame.add(setupMenuButtons(), BorderLayout.NORTH);
    }

    private JPanel setupControls() {
        // Card layout enables switching between category controls in place
        controlPanel = new JPanel(new CardLayout());

        controlPanel.add(foodControls(), LABEL_F);
        controlPanel.add(controlsB, LABEL_B);
        controlPanel.add(controlsM, LABEL_M);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(controlPanel);
        sidebar.add(setupCheckout());
        return sidebar;
    }

    private JPanel setupMenu() {
        // Table element will list menu items available to add to basket
        menuTable = new JTable(menu);

        // Staff can only add one menu item at a time as their properties differ
        // This model also forces a selection at all times for a consistent state
        menuTable.setSelectionModel(new MenuListSelectionModel());

        // Menu item controls differ per item, updated when list selection changes
        menuTable.getSelectionModel().addListSelectionListener(this::updateMenuControls);

        // Row sorter is used to apply filters to the table
        menuTable.setRowSorter(menuSorter);

        // Menu starts on food by default (always filtered to one type at a time)
        filterMenu(LABEL_F);

        // Scroll pane contains table to enable scrollbar
        JScrollPane menuPane = new JScrollPane();
        menuPane.setViewportView(menuTable);

        // Add label to distinguish UI sections
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Menu:"));
        panel.add(menuPane);
        return panel;
    }

    private JPanel setupBasket() {
        // Table element will list menu items available to add to basket
        JTable basketTable = new JTable(basket);

        // Scroll pane contains table to enable scrollbar
        JScrollPane basketPane = new JScrollPane();
        basketPane.setViewportView(basketTable);

        // Add label to distinguish UI sections
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Basket:"));
        panel.add(basketPane);
        return panel;
    }

    private JPanel setupMenuButtons() {
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
        return buttonPanel;
    }

    private JPanel setupCheckout() {
        JPanel panel = new JPanel();

        JButton add = new JButton("Add to Basket");
        JButton checkout = new JButton("Checkout");

        add.addActionListener(this::addToBasket);
        checkout.addActionListener(this::onCheckout);

        panel.add(add);
        panel.add(checkout);

        return panel;
    }

    private JPanel foodControls() {
        JPanel panel = new JPanel();

        // TODO show dietary classes
        panel.add(new JLabel("No food controls to show."));

        return panel;
    }

    // Instantiates a new order from item controls and puts it in the basket
    private void addToBasket(ActionEvent e) {
        OrderItem newItem;

        String itemId = selectedItem.getID();
        BigDecimal price = selectedItem.getPrice();
        String details = "";

        // Curent view dictates which item controls are relevant to generate item
        // details
        switch (menuView) {
            case LABEL_B:
                details = controlsB.getItemDetails();
                break;
            case LABEL_M:
                details = controlsM.getItemDetails();
                break;
            default:
                // Simple types (like food) have no specific details
                break;
        }

        newItem = new OrderItem(itemId, details, price, price);
        Order newOrder = new Order(LocalDateTime.now(), customer, newItem);

        basket.add(newOrder);

        // Refresh the UI table to reflect data change
        basket.fireTableDataChanged();
    }

    // Tells the basket to log all orders in the order history
    private void onCheckout(ActionEvent e) {
        basket.checkout();
        customer = nextCustomer();

        // Refresh the UI table to reflect data change
        basket.fireTableDataChanged();
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
            int i = e.getFirstIndex();

            // Index is in terms of the view, does not correspond to underlying data
            i = menuTable.convertRowIndexToModel(i);

            selectedItem = menu.getRowItem(i);

            // Controls to populate depend on the type of item
            if (selectedItem instanceof Food) {
                // TODO: Show dietary classes
            } else if (selectedItem instanceof Beverage) {
                controlsB.populate((Beverage) selectedItem);
            } else {
                controlsM.populate((Merchandise) selectedItem);
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

        // Table should always have an element selected for consistency
        menuTable.setRowSelectionInterval(0, 0);

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