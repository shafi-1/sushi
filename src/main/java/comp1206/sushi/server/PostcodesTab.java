package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PostcodesTab extends AddDeleteTemplate {

    private List<Postcode> postcodesList;
    private List<String> addedPostcodes;
    private List<Postcode> deletedPostcodes;
    private JTextField nameField;
    private Integer columnNamesLength;

    public PostcodesTab(List<Postcode> postcodes) {
        this.setPostcodesList(postcodes);
        super.createTemplate();
        setAddedPostcodes(new ArrayList<>());
        setDeletedPostcodes(new ArrayList<>());
        add.addActionListener(new AddListener());
        delete.addActionListener(new DeleteListener());
    }

    public JTable createTable() {
        String[] columnNames = {"Postcode", "Latitude", "Longitude", "Distance"};
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
        Object[][] data = new Object[getPostcodesList().size()][getColumnNamesLength()];

        for (int i = 0; i < getPostcodesList().size(); i++) {
            data[i][0] = getPostcodesList().get(i).getName();
            data[i][1] = getPostcodesList().get(i).getLatLong().get("lat");
            data[i][2] = getPostcodesList().get(i).getLatLong().get("lon");
            data[i][3] = getPostcodesList().get(i).getDistance();
        }
        System.out.println();
        return data;
    }

    @Override
    JPanel createButtons() {
        JLabel name = new JLabel("Postcode:");
        buttons.add(name, gbc);
        setNameField(new JTextField());
        buttons.add(getNameField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        return buttons;

    }

    public List<Postcode> getPostcodesList() {
        return postcodesList;
    }

    public void setPostcodesList(List<Postcode> postcodesList) {
        this.postcodesList = postcodesList;
    }

    public List<String> getAddedPostcodes() {
        return addedPostcodes;
    }

    public void setAddedPostcodes(List<String> addedPostcodes) {
        this.addedPostcodes = addedPostcodes;
    }

    public List<Postcode> getDeletedPostcodes() {
        return deletedPostcodes;
    }

    public void setDeletedPostcodes(List<Postcode> deletedPostcodes) {
        this.deletedPostcodes = deletedPostcodes;
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

    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{

            if (!getNameField().getText().equals("")) {
                boolean repeat = false;
                if (!getAddedPostcodes().contains(getNameField().getText())) {
                    for (Postcode p : getPostcodesList()) {
                        if (p.getName().equals(getNameField().getText())) {
                            repeat = true;
                        }
                    }
                    if (!repeat) {
                        String postcode = getNameField().getText();
                        getAddedPostcodes().add(postcode);
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
                Postcode value = getPostcodesList().get(row);
                getDeletedPostcodes().add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Postcode> returnDeletedPostcodes() {
        List<Postcode> temp = getDeletedPostcodes();
        setDeletedPostcodes(new ArrayList<>());
        return temp;
    }

    public List<String> returnAddedPostcodes() {
        List<String> temp = getAddedPostcodes();
        setAddedPostcodes(new ArrayList<>());
        return temp;
    }

    public void refreshPostcodes(List<Postcode> postcodes) {
        this.setPostcodesList(postcodes);

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