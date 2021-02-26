package sim.coffee;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsBeverage extends JPanel {

    // This is just to placate the linter. We never seralise this class so it's
    // irrelevant to us.
    private static final long serialVersionUID = 1L;

    JCheckBox isHot = new JCheckBox("Hot drink?");
    JComboBox<Size> sizes = new JComboBox<>();
    JComboBox<Milk> milks = new JComboBox<>();

    JLabel price = new JLabel("£0.00");

    // Track current item populating the controls
    // Used for price updates
    transient Beverage currentItem;

    ControlsBeverage() {
        // Grid bag layout allows easily stacking controls horizontally and vertically
        // while controlling their size
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Want components to expand horizontally as needed
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // This places all controls in one column vertically
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        this.add(isHot, gbc);
        this.add(new JLabel("Selected Size"), gbc);
        this.add(sizes, gbc);
        this.add(new JLabel("Selected Milk"), gbc);
        this.add(milks, gbc);

        // Place price label beside price at bottom of panel
        gbc.anchor = GridBagConstraints.PAGE_END;
        this.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(price, gbc);

        // Price changes with size and milk
        sizes.addActionListener(e -> updatePrice());
        milks.addActionListener(e -> updatePrice());
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Beverage b) {
        currentItem = b;

        Size[] availablSizes = b.getSizes();
        Milk[] availableMilks = b.getMilks();

        sizes.removeAllItems();
        for (Size s : availablSizes) {
            sizes.addItem(s);
        }

        milks.removeAllItems();
        for (Milk m : availableMilks) {
            milks.addItem(m);
        }

        // Hot drinks should default to hot and cold drinks should never be hot
        isHot.setEnabled(b.canBeHot());
        isHot.setSelected(b.canBeHot());

        sizes.setSelectedIndex(0);
        milks.setSelectedIndex(0);

        // No reason to interact with combo boxes when there's 1 option
        sizes.setEnabled(availablSizes.length > 1);
        milks.setEnabled(availableMilks.length > 1);

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
        // Can't update price while in temporary inconsistent state
        // This occurs when items are removed from the lists on population
        if (
            sizes.getSelectedIndex() == -1
            || milks.getSelectedIndex() == -1
        ) {
            return;
        }

        Size s = (Size) sizes.getSelectedItem();
        Milk m = (Milk) milks.getSelectedItem();

        // Prices should always display to 2 decimal places
        // Half even typically rounding method for finance
        BigDecimal newPrice = currentItem.getPrice(s, m).setScale(2, RoundingMode.HALF_EVEN);

        price.setText("£" + newPrice.toString());
    }
}
