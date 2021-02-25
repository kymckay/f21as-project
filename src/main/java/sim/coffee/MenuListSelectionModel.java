package sim.coffee;

import javax.swing.ListSelectionModel;
import javax.swing.DefaultListSelectionModel;

class MenuListSelectionModel extends DefaultListSelectionModel {

    // Menu is browsed one item at a time
    MenuListSelectionModel() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
        // Disable this to force a selection always
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
        // Disable this to force a selection always
    }

}