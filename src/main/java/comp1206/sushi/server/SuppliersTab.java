package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SuppliersTab extends SuppIngredTemplate {

    private List<Supplier> suppliersList;
    private List<Postcode> postcodesList;
    private List<Supplier> deletedSuppliers;
    private Map<String, Postcode> addedSuppliers;
    private Integer columnNamesLength;
    private JTextField nameField;
    private JComboBox jComboBox;

    public SuppliersTab(List<Supplier> suppliers, List<Postcode> postcodes) {
        this.setSuppliersList(suppliers);
        this.setPostcodesList(postcodes);
        super.createTemplate();
        setAddedSuppliers(new HashMap<>());
        setDeletedSuppliers(new ArrayList<>());
    }

    public JTable createTable() {
        String[] columnNames = {"Name", "Postcode", "Distance"};
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
        Object[][] data = new Object[getSuppliersList().size()][getColumnNamesLength()];

        for (int i = 0; i < getSuppliersList().size(); i++) {
            data[i][0] = getSuppliersList().get(i).getName();
            data[i][1] = getSuppliersList().get(i).getPostcode();
            data[i][2] = getSuppliersList().get(i).getDistance();
        }

        return data;
    }

    @Override
    public JPanel createButtons() {
        JLabel name = new JLabel("Name:");
        buttons.add(name, gbc);
        setNameField(new JTextField());
        buttons.add(getNameField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        JLabel postcode = new JLabel("Postcode:");
        buttons.add(postcode, gbc);

        setjComboBox(new JComboBox(getPostcodesList().toArray()));

        buttons.add(getjComboBox(), gbc);

        buttons.add(Box.createRigidArea(new Dimension(100,50)), gbc);

        JButton add = new JButton("Add");
        add.addActionListener(new AddListener());
        buttons.add(add, gbc);

        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        JButton delete = new JButton("Delete");
        delete.addActionListener(new DeleteListener());
        buttons.add(delete, gbc);

        return buttons;
    }

    public List<Supplier> getSuppliersList() {
        return suppliersList;
    }

    public void setSuppliersList(List<Supplier> suppliersList) {
        this.suppliersList = suppliersList;
    }

    public List<Postcode> getPostcodesList() {
        return postcodesList;
    }

    public void setPostcodesList(List<Postcode> postcodesList) {
        this.postcodesList = postcodesList;
    }

    public List<Supplier> getDeletedSuppliers() {
        return deletedSuppliers;
    }

    public void setDeletedSuppliers(List<Supplier> deletedSuppliers) {
        this.deletedSuppliers = deletedSuppliers;
    }

    public Map<String, Postcode> getAddedSuppliers() {
        return addedSuppliers;
    }

    public void setAddedSuppliers(Map<String, Postcode> addedSuppliers) {
        this.addedSuppliers = addedSuppliers;
    }

    public Integer getColumnNamesLength() {
        return columnNamesLength;
    }

    public void setColumnNamesLength(Integer columnNamesLength) {
        this.columnNamesLength = columnNamesLength;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public JComboBox getjComboBox() {
        return jComboBox;
    }

    public void setjComboBox(JComboBox jComboBox) {
        this.jComboBox = jComboBox;
    }

    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!getNameField().getText().equals("")) {
                boolean repeat = false;
                if (!getAddedSuppliers().containsKey(getNameField().getText())) {
                    for (Supplier s : getSuppliersList()) {
                        if (s.getName().equals(getNameField().getText())) {
                            repeat = true;
                        }
                    }
                    if (!repeat) {
                        String supplier = getNameField().getText();
                        Postcode postcode = (Postcode) getjComboBox().getSelectedItem();
                        getAddedSuppliers().put(supplier, postcode);
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
                Supplier value = getSuppliersList().get(row);
                getDeletedSuppliers().add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Supplier> returnDeletedSuppliers() {
        List<Supplier> temp = getDeletedSuppliers();
        setDeletedSuppliers(new ArrayList<>());
        return temp;
    }

    public Map<String, Postcode> returnAddedSuppliers() {
        Map<String, Postcode> temp = getAddedSuppliers();
        setAddedSuppliers(new HashMap<>());
        return temp;
    }

    public void refreshSuppliers(List<Supplier> suppliers, List<Postcode> postcodes) {
        this.setPostcodesList(postcodes);
        this.setSuppliersList(suppliers);

        Postcode temp = (Postcode) getjComboBox().getSelectedItem();

        getjComboBox().removeAllItems();

        for (Postcode p : postcodes) {
            getjComboBox().addItem(p);
        }

        if (postcodes.contains(temp)) {
            getjComboBox().setSelectedItem(temp);
        }

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