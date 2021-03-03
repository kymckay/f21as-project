package sim.coffee;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

public class MenuTableModel extends AbstractTableModel {

    // This is just to placate the linter. We never seralise this class so it's
    // irrelevant to us.
    private static final long serialVersionUID = 1L;

    private String[] columns = new String[] { "ID", "Item", "Base Price" };
    private String[] rowIDs;
    private transient Menu menu;

    public MenuTableModel(Menu menu) {
        this.menu = menu;
        this.rowIDs = menu.keySet().toArray(String[]::new);
	}

	@Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        // Column class determines row sorting behaviour
        // Without this, prices would incorrectly sort as strings
        if (columns[column].equals("Price")) {
            return BigDecimal.class;
        }

        // Any other column can safely be treated as a string
        return String.class;
    }

    @Override
    public int getRowCount() {
        return rowIDs.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MenuItem rowItem = getRowItem(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowItem.getID();
            case 1:
                return rowItem.getDescription();
            case 2:
                return rowItem.getPrice();
            default:
                // Should never be reached
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    public MenuItem getRowItem(int index) {
        return menu.getItem(rowIDs[index]);
    }
}
