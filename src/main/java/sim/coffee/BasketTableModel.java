package sim.coffee;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial") // Not planning on serializing this
public class BasketTableModel extends AbstractTableModel {

    private String[] columns = new String[] { "ID", "Item", "Price", "Discount" };
    OrderBasket basket;

    BasketTableModel(OrderBasket basket) {
        this.basket = basket;
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
        return basket.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order rowOrder = basket.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowOrder.getCustomerID();
            case 1:
                return rowOrder.getItemDetails();
            case 2:
                return rowOrder.getFullPrice();
            case 3:
                return String.format("%s%%",
                        rowOrder.getPricePaid().divide(rowOrder.getFullPrice()).multiply(new BigDecimal("100")));
            default:
                // Should never be reached
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    public void checkout() {
        basket.checkout();
    }
}
