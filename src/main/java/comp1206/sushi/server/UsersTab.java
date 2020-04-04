package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class UsersTab extends OrderUserTemplate{

    private List<User> usersList;
    private Integer columnNamesLength;
    private JTable jTable;

    public UsersTab(List<User> users) {
        this.setUsersList(users);
        super.createTemplate();
    }

    @Override
    JTable createTable() {
        String[] columnNames = {"Name", "Distance", "Postcode"};
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
        Object[][] data = new Object[getUsersList().size()][getColumnNamesLength()];

        for (int i = 0; i < getUsersList().size(); i++) {
            data[i][0] = getUsersList().get(i).getName();
            data[i][1] = getUsersList().get(i).getDistance();
            data[i][2] = getUsersList().get(i).getPostcode();
        }
        return data;
    }

    public void refreshUsers(List<User> users) {
        this.setUsersList(users);

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

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public Integer getColumnNamesLength() {
        return columnNamesLength;
    }

    public void setColumnNamesLength(Integer columnNamesLength) {
        this.columnNamesLength = columnNamesLength;
    }

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

}