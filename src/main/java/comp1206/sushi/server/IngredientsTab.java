package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class IngredientsTab extends SuppIngredTemplate {

    private List<Ingredient> ingredientsList;
    private List<Supplier> suppliersList;
    private List<Object[]> addedIngredients;
    private List<Ingredient> deletedIngredients;

    private Integer columnNamesLength;

    private ServerInterface server;

    private JTextField nameField;
    private JTextField unitField;
    private JTextField restockThresholdField;
    private JTextField restockAmountField;

    private JComboBox jComboBox;

    private JTextField editThresholdField;
    private JTextField editAmountField;

    public IngredientsTab(List<Ingredient> ingredients, List<Supplier> suppliers, ServerInterface server) {
        this.setIngredientsList(ingredients);
        this.setSuppliersList(suppliers);
        this.setServer(server);
        super.createTemplate();
        setAddedIngredients(new ArrayList<>());
        setDeletedIngredients(new ArrayList<>());
    }

    public JTable createTable() {
        String[] columnNames = {"Name", "Unit", "Supplier",
                "Restock Threshold", "Restock Amount", "Stock"};
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
        Object[][] data = new Object[getIngredientsList().size()][getColumnNamesLength()];

        for (int i = 0; i < getIngredientsList().size(); i++) {
            data[i][0] = getIngredientsList().get(i).getName();
            data[i][1] = getIngredientsList().get(i).getUnit();
            data[i][2] = getIngredientsList().get(i).getSupplier();
            data[i][3] = getIngredientsList().get(i).getRestockThreshold();
            data[i][4] = getIngredientsList().get(i).getRestockAmount();
            data[i][5] = getServer().getIngredientStockLevels().get(getIngredientsList().get(i));
        }

        return data;
    }

    public JPanel createButtons() {

        JLabel name = new JLabel("Name:");
        buttons.add(name, gbc);
        setNameField(new JTextField());
        buttons.add(getNameField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JLabel unit = new JLabel("Unit:");
        buttons.add(unit, gbc);
        setUnitField(new JTextField());
        buttons.add(getUnitField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JLabel restockThreshold = new JLabel("Restock Threshold:");
        buttons.add(restockThreshold, gbc);
        setRestockThresholdField(new JTextField());
        buttons.add(getRestockThresholdField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JLabel restockAmount = new JLabel("Restock Amount:");
        buttons.add(restockAmount, gbc);
        setRestockAmountField(new JTextField());
        buttons.add(getRestockAmountField(), gbc);

        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JLabel supplier = new JLabel("Supplier:");
        buttons.add(supplier, gbc);

        setjComboBox(new JComboBox(getSuppliersList().toArray()));

        buttons.add(getjComboBox(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);
        JButton add = new JButton("Add");
        add.addActionListener(new AddListener());
        buttons.add(add, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        JButton delete = new JButton("Delete");
        delete.addActionListener(new DeleteListener());
        buttons.add(delete, gbc);

        buttons.add(Box.createRigidArea(new Dimension(100,50)), gbc);

        JLabel editThreshold = new JLabel("Edit Restock Threshold:");
        buttons.add(editThreshold, gbc);
        setEditThresholdField(new JTextField());
        buttons.add(getEditThresholdField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        JButton editThresholdB = new JButton("Edit Threshold");
        buttons.add(editThresholdB, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        JLabel editAmount = new JLabel("Edit Restock Amount:");
        buttons.add(editAmount, gbc);
        setEditAmountField(new JTextField());
        buttons.add(getEditAmountField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        JButton editAmountB = new JButton("Edit Amount");
        buttons.add(editAmountB, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        editThresholdB.addActionListener(new ThresholdListener());
        editAmountB.addActionListener(new AmountListener());
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        return buttons;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<Supplier> getSuppliersList() {
        return suppliersList;
    }

    public void setSuppliersList(List<Supplier> suppliersList) {
        this.suppliersList = suppliersList;
    }

    public List<Object[]> getAddedIngredients() {
        return addedIngredients;
    }

    public void setAddedIngredients(List<Object[]> addedIngredients) {
        this.addedIngredients = addedIngredients;
    }

    public List<Ingredient> getDeletedIngredients() {
        return deletedIngredients;
    }

    public void setDeletedIngredients(List<Ingredient> deletedIngredients) {
        this.deletedIngredients = deletedIngredients;
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

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public JTextField getUnitField() {
        return unitField;
    }

    public void setUnitField(JTextField unitField) {
        this.unitField = unitField;
    }

    public JTextField getRestockThresholdField() {
        return restockThresholdField;
    }

    public void setRestockThresholdField(JTextField restockThresholdField) {
        this.restockThresholdField = restockThresholdField;
    }

    public JTextField getRestockAmountField() {
        return restockAmountField;
    }

    public void setRestockAmountField(JTextField restockAmountField) {
        this.restockAmountField = restockAmountField;
    }

    public JComboBox getjComboBox() {
        return jComboBox;
    }

    public void setjComboBox(JComboBox jComboBox) {
        this.jComboBox = jComboBox;
    }

    public JTextField getEditThresholdField() {
        return editThresholdField;
    }

    public void setEditThresholdField(JTextField editThresholdField) {
        this.editThresholdField = editThresholdField;
    }

    public JTextField getEditAmountField() {
        return editAmountField;
    }

    public void setEditAmountField(JTextField editAmountField) {
        this.editAmountField = editAmountField;
    }


    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!getNameField().getText().equals("") && (!getUnitField().getText().equals(""))
                    && (!getRestockThresholdField().getText().equals(""))
                    && (!getRestockAmountField().getText().equals(""))) {
                String name = getNameField().getText();
                String unit = getUnitField().getText();
                Supplier supplier = (Supplier) getjComboBox().getSelectedItem();
                Integer restockThreshold = Integer.valueOf(getRestockThresholdField().getText());
                Integer restockAmount = Integer.valueOf(getRestockAmountField().getText());
                getAddedIngredients().add(new Object[]{name, unit, supplier, restockThreshold, restockAmount});
                getNameField().setText("");
                getUnitField().setText("");
                getRestockThresholdField().setText("");
                getRestockAmountField().setText("");
            }
        }
    }

    private class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTable.getSelectedRow();
            try {
                Ingredient value = getIngredientsList().get(row);
                getDeletedIngredients().add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Ingredient> returnDeletedIngredients() {
        List<Ingredient> temp = getDeletedIngredients();
        setDeletedIngredients(new ArrayList<>());
        return temp;
    }


    private class ThresholdListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!getEditThresholdField().getText().equals("")) {
                int row = jTable.getSelectedRow();
                Ingredient value = getIngredientsList().get(row);
                Integer editThreshold = Integer.valueOf(getEditThresholdField().getText());
                value.setRestockThreshold(editThreshold);
                getEditThresholdField().setText("");
            }
        }
    }

    private class AmountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!getEditThresholdField().getText().equals("")) {
                int row = jTable.getSelectedRow();
                Ingredient value = getIngredientsList().get(row);
                Integer editAmount = Integer.valueOf(getEditAmountField().getText());
                value.setRestockAmount(editAmount);
                getEditAmountField().setText("");
            }
        }
    }

    public List<Object[]> returnAddedIngredients() {
        List<Object[]>  temp = getAddedIngredients();
        setAddedIngredients(new ArrayList<>());
        return temp;
    }

    public void refreshIngredients(List<Ingredient> ingredients, List<Supplier> suppliers) {
        this.setIngredientsList(ingredients);
        this.setSuppliersList(suppliers);

        Supplier temp = (Supplier) getjComboBox().getSelectedItem();

        getjComboBox().removeAllItems();

        for (Supplier s : suppliers) {
            getjComboBox().addItem(s);
        }

        if (suppliers.contains(temp)) {
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