package sim.coffee;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ControlsMerchandise extends JPanel {

    JComboBox<Colour> colours = new JComboBox<>();
    JComboBox<Label> labels = new JComboBox<>();

    public ControlsMerchandise() {
        this.add(colours);
        this.add(labels);
    }

    // Populates the item controls based on the supplied menu item
    public void populate(Merchandise m) {
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
    }

    public String getItemDetails() {
        Label l = (Label) labels.getSelectedItem();
        Colour c = (Colour) colours.getSelectedItem();

        return Merchandise.formatDetails(l, c);
    }
}
