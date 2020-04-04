package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class StaffTab extends AddDeleteTemplate {

    private List<Staff> staffList;
    private List<String> addedStaff;
    private List<Staff> deletedStaff;
    private JTextField nameField;
    private Integer columnNamesLength;
    private ServerInterface server;

    public StaffTab(List<Staff> staff, ServerInterface server) {
        this.setStaffList(staff);
        this.setServer(server);
        super.createTemplate();
        setAddedStaff(new ArrayList<>());
        setDeletedStaff(new ArrayList<>());
        add.addActionListener(new AddListener());
        delete.addActionListener(new DeleteListener());
    }

    public JTable createTable() {
        String[] columnNames = {"Name", "Status", "Fatigue"};
        this.setColumnNamesLength(columnNames.length);

        Object[][] data = retrieveData();

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable jTable = new JTable();
        jTable.setModel(tableModel);
        return jTable;
    }

    public Object[][] retrieveData() {
        Object[][] data = new Object[getStaffList().size()][getColumnNamesLength()];

        for (int i = 0; i < getStaffList().size(); i++) {
            data[i][0] = getStaffList().get(i).getName();
            data[i][1] = getServer().getStaffStatus(getStaffList().get(i));
            data[i][2] = getStaffList().get(i).getFatigue();
        }

        return data;
    }

    @Override
    JPanel createButtons() {
        JLabel name = new JLabel("Name:");
        buttons.add(name, gbc);
        setNameField(new JTextField());
        buttons.add(getNameField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        return buttons;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public List<String> getAddedStaff() {
        return addedStaff;
    }

    public void setAddedStaff(List<String> addedStaff) {
        this.addedStaff = addedStaff;
    }

    public List<Staff> getDeletedStaff() {
        return deletedStaff;
    }

    public void setDeletedStaff(List<Staff> deletedStaff) {
        this.deletedStaff = deletedStaff;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
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

    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!getNameField().getText().equals("")) {
                boolean repeat = false;
                if (!getAddedStaff().contains(getNameField().getText())) {
                    for (Staff s : getStaffList()) {
                        if (s.getName().equals(getNameField().getText())) {
                            repeat = true;
                        }
                    }
                    if (!repeat) {
                        String name = getNameField().getText();
                        getAddedStaff().add(name);
                    }
                }
                getNameField().setText("");
            }
        }
    }

    private class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTable.getSelectedRow();
            try {
                Staff value = getStaffList().get(row);
                getDeletedStaff().add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Staff> returnDeletedStaff() {
        List<Staff> temp = getDeletedStaff();
        setDeletedStaff(new ArrayList<>());
        return temp;
    }

    public List<String> returnAddedStaff() {
        List<String> temp = getAddedStaff();
        setAddedStaff(new ArrayList<>());
        return temp;
    }

    public void refreshStaff(List<Staff> staff) {
        this.setStaffList(staff);

        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        model.getDataVector().removeAllElements();

        Object[][] data = retrieveData();
        for (int i = 0; i < data.length; i++) {
            model.addRow(data[i]);
        }

        model.fireTableDataChanged();
        jTable.setModel(model);
    }

}