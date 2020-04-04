package comp1206.sushi.server;

import javax.swing.*;
import java.awt.*;

public abstract class OrderUserTemplate extends JPanel {

    private JTable jTable;

    abstract JTable createTable();

    abstract Object[][] retrieveData();

    public OrderUserTemplate() {

    }

    public void createTemplate() {
        this.setLayout(new BorderLayout());
        this.jTable = createTable();

        this.add(jTable.getTableHeader(), BorderLayout.NORTH);
        this.add(jTable, BorderLayout.CENTER);
    }

}