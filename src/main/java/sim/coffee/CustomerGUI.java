package sim.coffee;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Dimension;

import javax.swing.JFrame;

public class CustomerGUI {
    private MenuTableModel menu;
    private OrderList processed;
    private OrderTableModel basket;

    CustomerGUI(MenuTableModel menu, OrderList processed, OrderTableModel basket) {
        this.menu = menu;
        this.processed = processed;
        this.basket = basket;

        JFrame guiFrame = new JFrame("Order Processing");

        // Prevent making the window too small to use
        guiFrame.setMinimumSize(new Dimension(500, 500));

        guiFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Let frame layout manager handle sizing
        guiFrame.pack();
        guiFrame.setVisible(true);
    }
}