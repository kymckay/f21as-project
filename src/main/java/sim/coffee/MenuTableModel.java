package sim.coffee;

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
