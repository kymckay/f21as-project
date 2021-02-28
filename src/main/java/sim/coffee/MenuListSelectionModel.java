package sim.coffee;

import javax.swing.ListSelectionModel;
import javax.swing.DefaultListSelectionModel;

class MenuListSelectionModel extends DefaultListSelectionModel {

    // This is just to placate the linter. We never seralise this class so it's
    // irrelevant to us.
    private static final long serialVersionUID = 1L;

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