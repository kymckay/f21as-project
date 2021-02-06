package sim.coffee;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial") // Not planning on serializing this
public class OrderTableModel extends AbstractTableModel {

    private String[] columns = new String[] { "ID", "Item", "Price" };
    OrderList orderList;

    OrderTableModel() {
        this(new OrderList());
    }

    OrderTableModel(OrderList orders) {
        this.orderList = orders;
    }

	@Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return orderList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order rowOrder = orderList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowOrder.getCustomerID();
            case 1:
                return rowOrder.getItemDetails().displayString();
            case 2:
                return rowOrder.getPricePaid();
            default:
                // Should never be reached
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

}
