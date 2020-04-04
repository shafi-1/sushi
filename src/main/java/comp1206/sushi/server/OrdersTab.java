package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class OrdersTab extends OrderUserTemplate{

    private List<Order> ordersList;
    private Integer columnNamesLength;
    private ServerInterface server;
    private JTable jTable;

    public OrdersTab(List<Order> orders, ServerInterface server) {
        this.setOrdersList(orders);
        this.setServer(server);
        super.createTemplate();
    }

    @Override
    JTable createTable() {
        String[] columnNames = {"Name", "Distance", "Status", "Price"};
        this.setColumnNamesLength(columnNames.length);

        Object[][] data = retrieveData();

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setjTable(new JTable());
        getjTable().setModel(tableModel);
        return getjTable();
    }

    public Object[][] retrieveData() {
        Object[][] data = new Object[getOrdersList().size()][getColumnNamesLength()];

        for (int i = 0; i < getOrdersList().size(); i++) {
            data[i][0] = getOrdersList().get(i).getName();
            data[i][1] = getServer().getOrderDistance(getOrdersList().get(i));
            data[i][2] = getServer().getOrderStatus(getOrdersList().get(i));
            data[i][3] = getServer().getOrderCost(getOrdersList().get(i));
        }
        return data;
    }

    public void refreshOrders(List<Order> orders) {
        this.setOrdersList(orders);

        DefaultTableModel model = (DefaultTableModel) getjTable().getModel();
        model.getDataVector().removeAllElements();

        Object[][] data = retrieveData();
        for (int i = 0; i < data.length; i++) {
            model.addRow(data[i]);
        }

        model.fireTableDataChanged();
        model.fireTableStructureChanged();
        getjTable().setModel(model);
    }

    public List<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    public Integer getColumnNamesLength() {
        return columnNamesLength;
    }

    public void setColumnNamesLength(Integer columnNamesLength) {
        this.columnNamesLength = columnNamesLength;
    }

    public ServerInterface getServer() {
        return server;
    }

    public void setServer(ServerInterface server) {
        this.server = server;
    }

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

}