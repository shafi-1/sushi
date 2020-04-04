package comp1206.sushi.server;

import comp1206.sushi.common.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DronesTab extends AddDeleteTemplate {

    private List<Drone> dronesList;
    private List<Integer> addedDrones;
    private List<Drone> deletedDrones;
    private JTextField speedField;
    private Integer columnNamesLength;
    private ServerInterface server;

    public DronesTab(List<Drone> drones, ServerInterface server) {
        this.setDronesList(drones);
        this.setServer(server);
        super.createTemplate();
        setAddedDrones(new ArrayList<>());
        setDeletedDrones(new ArrayList<>());
        add.addActionListener(new AddListener());
        delete.addActionListener(new DeleteListener());
    }

    public JTable createTable() {
        String[] columnNames = {"Name", "Status", "Speed", "Capacity",
                "Battery", "Source", "Destination", "Progress"};
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
        Object[][] data = new Object[getDronesList().size()][getColumnNamesLength()];

        for (int i = 0; i < getDronesList().size(); i++) {
            data[i][0] = getDronesList().get(i).getName();
            data[i][1] = getServer().getDroneStatus(getDronesList().get(i));
            data[i][2] = getServer().getDroneSpeed(getDronesList().get(i));
            data[i][3] = getDronesList().get(i).getCapacity();
            data[i][4] = getDronesList().get(i).getBattery();
            data[i][5] = getServer().getDroneSource(getDronesList().get(i));
            data[i][6] = getServer().getDroneDestination(getDronesList().get(i));
            data[i][7] = getServer().getDroneProgress(getDronesList().get(i));
        }

        return data;
    }

    @Override
    JPanel createButtons() {

        JLabel speed = new JLabel("Speed:");
        buttons.add(speed, gbc);
        setSpeedField(new JTextField(10));
        buttons.add(getSpeedField(), gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        return buttons;
    }

    public List<Drone> getDronesList() {
        return dronesList;
    }

    public void setDronesList(List<Drone> dronesList) {
        this.dronesList = dronesList;
    }

    public List<Integer> getAddedDrones() {
        return addedDrones;
    }

    public void setAddedDrones(List<Integer> addedDrones) {
        this.addedDrones = addedDrones;
    }

    public List<Drone> getDeletedDrones() {
        return deletedDrones;
    }

    public void setDeletedDrones(List<Drone> deletedDrones) {
        this.deletedDrones = deletedDrones;
    }

    public JTextField getSpeedField() {
        return speedField;
    }

    public void setSpeedField(JTextField speedField) {
        this.speedField = speedField;
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
            if (!getSpeedField().getText().equals("")) {
                Integer speed = Integer.valueOf(getSpeedField().getText());
                getAddedDrones().add(speed);
                getSpeedField().setText("");
            }
        }
    }

    private class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTable.getSelectedRow();
            try {
                Drone value = getDronesList().get(row);
                getDeletedDrones().add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Drone> returnDeletedDrones() {
        List<Drone> temp = getDeletedDrones();
        setDeletedDrones(new ArrayList<>());
        return temp;
    }

    public List<Integer> returnAddedDrones() {
        List<Integer> temp = getAddedDrones();
        setAddedDrones(new ArrayList<>());
        return temp;
    }

    public void refreshDrones(List<Drone> drones) {
        this.setDronesList(drones);

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