package sim.views;

import java.awt.Font;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import sim.interfaces.Observer;
import sim.model.Customer;
import sim.model.Kitchen;
import sim.model.MenuItem;

public class KitchenGUI extends JTextArea implements Observer {
    private Kitchen kitchen;

    public KitchenGUI(Kitchen kitchen) {
        this.kitchen = kitchen;
        kitchen.registerObserver(this);

        setup();
    }

    public void setup() {
        setEditable(false);
        setText("Waiting for orders...");
        setFont(new Font("Monospaced", Font.PLAIN, 13));
        Border border = BorderFactory.createTitledBorder("Kitchen");
        setBorder(border);
    }

    @Override
    public void update() {
        Optional<Customer> customer = kitchen.getCurrentCustomer();

        if (customer.isPresent()) {
            Customer serving = customer.get();

            StringBuilder currentOrder = new StringBuilder();

            currentOrder.replace(0, currentOrder.length(), "");
            currentOrder.append("Customer being served: \n");
            currentOrder.append("   " + serving.getName() + "\n");
            currentOrder.append("Ordered items: \n");
            for (MenuItem o : serving.getOrder()) {
                // capitalize the order name
                currentOrder.append("   " + Character.toUpperCase(o.getName().charAt(0)) + o.getName().substring(1) + "\n");
            }
            setText(currentOrder.toString());
        } else {
            setText("Waiting for orders...");
        }
    }
}
