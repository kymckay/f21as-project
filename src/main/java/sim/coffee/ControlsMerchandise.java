package sim.coffee;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsMerchandise extends JPanel {

    // This is just to placate the linter. We never seralise this class so it's
    // irrelevant to us.
    private static final long serialVersionUID = 1L;

    JComboBox<Colour> colours = new JComboBox<>();
    JComboBox<Label> labels = new JComboBox<>();

    JLabel price = new JLabel("£0.00");

    // Track current item populating the controls
    // Used for price updates
    transient Merchandise currentItem;

    public ControlsMerchandise() {
        // Grid bag layout allows easily stacking controls horizontally and vertically
        // while controlling their size
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Want components to expand horizontally as needed
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // This places all controls in one column vertically
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        this.add(new JLabel("Selected Colour"), gbc);
        this.add(colours, gbc);
        this.add(new JLabel("Selected Label"), gbc);
        this.add(labels, gbc);

        // Place price label beside price at bottom of panel
        gbc.anchor = GridBagConstraints.PAGE_END;
        this.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(price, gbc);

        // Price changes with label
        labels.addActionListener(e -> updatePrice());
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Merchandise m) {
        currentItem = m;

        Colour[] availableColours = m.getColours();
        Label[] availableLabels = m.getLabels();

        colours.removeAllItems();
        for (Colour c : availableColours) {
            colours.addItem(c);
        }

        labels.removeAllItems();
        for (Label l : availableLabels) {
            labels.addItem(l);
        }

        colours.setSelectedIndex(0);
        labels.setSelectedIndex(0);

        // No reason to interact with combo boxes when there's 1 option
        colours.setEnabled(availableColours.length > 1);
        labels.setEnabled(availableLabels.length > 1);

        // New item's initial price probably differs from previous
        updatePrice();
    }

    public String getItemDetails() {
        Label l = (Label) labels.getSelectedItem();
        Colour c = (Colour) colours.getSelectedItem();

        return Merchandise.formatDetails(l, c);
    }

    private void updatePrice() {
        // Can't update price while in temporary inconsistent state
        // This occurs when items are removed from the list on population
        if (labels.getSelectedIndex() == -1) {
            return;
        }

        Label l = (Label) labels.getSelectedItem();

        // Price already given to the right accuracy
        BigDecimal newPrice = currentItem.getPrice(l);

        price.setText("£" + newPrice.toString());
    }
}
