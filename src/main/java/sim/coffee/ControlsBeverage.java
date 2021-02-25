package sim.coffee;

import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsBeverage extends JPanel {

    JCheckBox isHot = new JCheckBox();
    JComboBox<Size> sizes = new JComboBox<>();
    JComboBox<Milk> milks = new JComboBox<>();

    JLabel price = new JLabel("£0.00");

    // Track current item populating the controls
    // Used for price updates
    Beverage currentItem;

    ControlsBeverage() {
        this.add(isHot);
        this.add(sizes);
        this.add(milks);
        this.add(price);
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Beverage b) {
        currentItem = b;

        sizes.removeAllItems();
        for (Size s : b.getSizes()) {
            sizes.addItem(s);
        }

        milks.removeAllItems();
        for (Milk m : b.getMilks()) {
            milks.addItem(m);
        }

        // Hot drinks should default to hot and cold drinks should never be hot
        isHot.setEnabled(b.canBeHot());
        isHot.setSelected(b.canBeHot());

        sizes.setSelectedIndex(0);
        milks.setSelectedIndex(0);

        // Price changes with size and milk
        sizes.addActionListener(e -> updatePrice());
        milks.addActionListener(e -> updatePrice());

        // New item's initial price probably differs from previous
        updatePrice();
    }

    public String getItemDetails() {
        Size s = (Size) sizes.getSelectedItem();
        boolean hot = isHot.isSelected();
        Milk m = (Milk) milks.getSelectedItem();

        return Beverage.formatDetails(s, hot, m);
    }

    private void updatePrice() {
        Size s = (Size) sizes.getSelectedItem();
        Milk m = (Milk) milks.getSelectedItem();

        BigDecimal newPrice = currentItem.getPrice(s, m);

        price.setText(newPrice.toString());
    }
}
