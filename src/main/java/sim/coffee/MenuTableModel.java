package sim.coffee;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial") // Not planning on serializing this
public class MenuTableModel extends AbstractTableModel {

    private String[] columns = new String[] { "ID", "Item", "Price" };
    private String[] rowIDs;
    private Menu menu;

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
        MenuItem rowItem = menu.getKey(rowIDs[rowIndex]);

        switch (columnIndex) {
            case 0:
                return rowItem.getId();
            case 1:
                return rowItem.getDescription();
            case 2:
                return rowItem.getBasePrice();
            default:
                // Should never be reached
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

}
