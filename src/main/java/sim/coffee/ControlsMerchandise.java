package sim.coffee;

import java.math.BigDecimal;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsMerchandise extends JPanel {

    JComboBox<Colour> colours = new JComboBox<>();
    JComboBox<Label> labels = new JComboBox<>();

    JLabel price = new JLabel("£0.00");

    // Track current item populating the controls
    // Used for price updates
    Merchandise currentItem;

    public ControlsMerchandise() {
        this.add(colours);
        this.add(labels);
        this.add(price);

        // Price changes with label
        labels.addActionListener(e -> updatePrice());
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Merchandise m) {
        currentItem = m;

        colours.removeAllItems();
        for (Colour c : m.getColours()) {
            colours.addItem(c);
        }

        labels.removeAllItems();
        for (Label l : m.getLabels()) {
            labels.addItem(l);
        }

        colours.setSelectedIndex(0);
        labels.setSelectedIndex(0);

        // New item's initial price probably differs from previous
        updatePrice();
    }

    public String getItemDetails() {
        Label l = (Label) labels.getSelectedItem();
        Colour c = (Colour) colours.getSelectedItem();

        return Merchandise.formatDetails(l, c);
    }

    private void updatePrice() {
        Label l = (Label) labels.getSelectedItem();

        BigDecimal newPrice = currentItem.getPrice(l);

        price.setText(newPrice.toString());
    }
}
