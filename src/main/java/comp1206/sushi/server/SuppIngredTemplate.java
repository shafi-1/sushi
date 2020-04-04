package comp1206.sushi.server;

import javax.swing.*;
import java.awt.*;

public abstract class SuppIngredTemplate extends JPanel {

    protected JPanel buttons;
    protected JPanel table;
    protected JTable jTable;
    protected GridBagConstraints gbc;

    abstract JTable createTable();
    abstract JPanel createButtons();

    public SuppIngredTemplate() {

    }

    public void createTemplate() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        buttons = new JPanel(new GridBagLayout());
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.buttons = createButtons();

        table = new JPanel();
        this.add(table, borderLayout.CENTER);
        this.add(buttons, borderLayout.WEST);
        table.setLayout(new BorderLayout());
        this.jTable = createTable();
        table.add(jTable, BorderLayout.CENTER);
        table.add(jTable.getTableHeader(), BorderLayout.NORTH);
    }

}