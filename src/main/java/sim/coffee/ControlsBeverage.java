package sim.coffee;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ControlsBeverage extends JPanel {

    JCheckBox isHot = new JCheckBox();
    JComboBox<Size> sizes = new JComboBox<>();
    JComboBox<Milk> milks = new JComboBox<>();

    ControlsBeverage() {
        this.add(isHot);
        this.add(sizes);
        this.add(milks);
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Beverage b) {
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
    }

    public String getItemDetails() {
        Size s = (Size) sizes.getSelectedItem();
        boolean hot = isHot.isSelected();
        Milk m = (Milk) milks.getSelectedItem();

        return Beverage.formatDetails(s, hot, m);
    }
}
