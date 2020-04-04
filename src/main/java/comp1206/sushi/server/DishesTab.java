package comp1206.sushi.server;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishesTab extends JPanel {

    private List<Dish> dishesList;
    private List<Object[]> addedDishes;
    private List<Ingredient> ingredientsList;
    private Map<Ingredient, Integer> addedIngredients;
    private List<Ingredient> tableOfIngredients;
    private Map<Dish, Ingredient>  deletedIngredients;
    private List<Dish> deletedDishes;

    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField restockThresholdField;
    private JTextField restockAmountField;

    private Integer columnNamesLength;

    private ServerInterface server;

    private JTable jTable;

    private JTextField editThresholdField;
    private JTextField editAmountField;

    private JComboBox ingredientCombo;
    private JTextField quantityField;
    private JTable recipeTable;
    private JDialog recipeWindow;

    private Dish recipeDish;


    public DishesTab(List<Dish> dishes, List<Ingredient> ingredients, ServerInterface server) {
        this.dishesList = dishes;
        this.ingredientsList = ingredients;
        this.server = server;
        jTable = new JTable();
        createTable();
        createTemplate();
        addedDishes = new ArrayList<>();
        addedIngredients = new HashMap<>();
        tableOfIngredients = new ArrayList<>();
        deletedIngredients = new HashMap<>();
        deletedDishes = new ArrayList<>();
    }

    public void createTemplate() {

        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton create = new JButton("Create");
        create.addActionListener(new CreateListener());

        JLabel name = new JLabel("Name:");
        buttons.add(name, gbc);
        nameField = new JTextField();
        buttons.add(nameField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 5)), gbc);

        JLabel description = new JLabel("Description:");
        buttons.add(description, gbc);
        descriptionField = new JTextField();
        buttons.add(descriptionField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 5)), gbc);

        JLabel price = new JLabel("Price:");
        buttons.add(price, gbc);
        priceField = new JTextField();
        buttons.add(priceField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 5)), gbc);

        JLabel restockThreshold = new JLabel("Restock Threshold:");
        buttons.add(restockThreshold, gbc);
        restockThresholdField = new JTextField();
        buttons.add(restockThresholdField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 5)), gbc);

        JLabel restockAmount = new JLabel("Restock Amount:");
        buttons.add(restockAmount, gbc);
        restockAmountField = new JTextField();
        buttons.add(restockAmountField, gbc);

        buttons.add(Box.createRigidArea(new Dimension(100, 25)), gbc);

        buttons.add(create, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 10)), gbc);
        JButton edit = new JButton("Edit");
        buttons.add(edit, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 10)), gbc);
        JButton delete = new JButton("Delete");
        delete.addActionListener(new DeleteDishListener());
        buttons.add(delete, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100, 10)), gbc);
        JButton recipe = new JButton("View Recipe");
        buttons.add(recipe, gbc);
        recipe.addActionListener(new RecipeListener());

        buttons.add(Box.createRigidArea(new Dimension(100, 25)), gbc);

        JLabel editThreshold = new JLabel("Edit Restock Threshold:");
        buttons.add(editThreshold, gbc);
        editThresholdField = new JTextField();
        buttons.add(editThresholdField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JButton editThresholdB = new JButton("Edit Threshold");
        buttons.add(editThresholdB, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,10)), gbc);

        JLabel editAmount = new JLabel("Edit Restock Amount:");
        buttons.add(editAmount, gbc);
        editAmountField = new JTextField();
        buttons.add(editAmountField, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,5)), gbc);

        JButton editAmountB = new JButton("Edit Amount");
        buttons.add(editAmountB, gbc);
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        editThresholdB.addActionListener(new ThresholdListener());
        editAmountB.addActionListener(new AmountListener());
        buttons.add(Box.createRigidArea(new Dimension(100,15)), gbc);

        JPanel table = new JPanel();
        table.setLayout(new BorderLayout());

        table.add(jTable, BorderLayout.CENTER);
        table.add(jTable.getTableHeader(), BorderLayout.NORTH);
        this.add(table, borderLayout.CENTER);
        table.add(jTable, borderLayout.CENTER);
        this.add(buttons, borderLayout.WEST);
    }

    public void createTable() {

        String[] columnNames = {"Name", "Description", "Recipe",
                "Price", "Restock Threshold", "Restock Amount", "Stock"};
        this.columnNamesLength = columnNames.length;

        Object[][] data = retrieveData();

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable.setModel(tableModel);
    }

    public Object[][] retrieveData() {
        Object[][] data = new Object[dishesList.size()][columnNamesLength];

        for (int i = 0; i < dishesList.size(); i++) {
            data[i][0] = dishesList.get(i).getName();
            data[i][1] = dishesList.get(i).getDescription();
            data[i][2] = dishesList.get(i).getRecipe();
            data[i][3] = dishesList.get(i).getPrice();
            data[i][4] = dishesList.get(i).getRestockThreshold();
            data[i][5] = dishesList.get(i).getRestockAmount();
            data[i][6] = server.getDishStockLevels().get(dishesList.get(i));
        }

        return data;
    }

    public ServerInterface getServer() {
        return server;
    }

    public JTable getjTable() {
        return jTable;
    }

    private class CreateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if ((!nameField.getText().equals("")) && (!descriptionField.getText().equals(""))
                    && (!priceField.getText().equals("")) && (!restockThresholdField.getText().equals(""))
                    && (!restockAmountField.getText().equals(""))) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                Integer price = Integer.valueOf(priceField.getText());
                Integer restockThreshold = Integer.valueOf(restockThresholdField.getText());
                Integer restockAmount = Integer.valueOf(restockAmountField.getText());
                addedDishes.add(new Object[]{name, description, price, restockThreshold, restockAmount});
                nameField.setText("");
                descriptionField.setText("");
                priceField.setText("");
                restockThresholdField.setText("");
                restockAmountField.setText("");
            }
        }
    }

    private class ThresholdListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!editThresholdField.getText().equals("")) {
                int row = jTable.getSelectedRow();
                Dish value = dishesList.get(row);
                Integer editThreshold = Integer.valueOf(editThresholdField.getText());
                value.setRestockThreshold(editThreshold);
            }
        }
    }

    private class AmountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            if (!editThresholdField.getText().equals("")) {
                int row = jTable.getSelectedRow();
                Dish value = dishesList.get(row);
                Integer editAmount = Integer.valueOf(editAmountField.getText());
                value.setRestockAmount(editAmount);
            }
        }
    }

    private class RecipeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
            try {
                tableOfIngredients = new ArrayList<>();
                int row = jTable.getSelectedRow();
                recipeDish = dishesList.get(row);

                recipeWindow = new JDialog();
                recipeWindow.setTitle(recipeDish.getName());

                JPanel recipePanel = new JPanel();
                recipeWindow.setContentPane(recipePanel);

                recipePanel.setLayout(new BorderLayout());

                JPanel recipeTablePanel = new JPanel(new BorderLayout());

                JPanel recipeButtonsPanel = new JPanel(new GridBagLayout());
                recipeButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                ingredientCombo = new JComboBox(ingredientsList.toArray());

                recipeButtonsPanel.add(ingredientCombo, gbc);
                recipeButtonsPanel.add(Box.createRigidArea(new Dimension(100, 15)), gbc);

                JLabel quantity = new JLabel("Quantity:");
                recipeButtonsPanel.add(quantity, gbc);
                quantityField = new JTextField();
                recipeButtonsPanel.add(quantityField, gbc);

                recipeButtonsPanel.add(Box.createRigidArea(new Dimension(100, 50)), gbc);

                JButton add = new JButton("Add");
                recipeButtonsPanel.add(add, gbc);
                add.addActionListener(new AddListener());

                recipeButtonsPanel.add(Box.createRigidArea(new Dimension(100, 15)), gbc);

                JButton delete = new JButton("Delete");
                recipeButtonsPanel.add(delete, gbc);
                delete.addActionListener(new DeleteListener());

                recipeTable = createRecipeTable(recipeDish);

                recipeTablePanel.add(recipeTable.getTableHeader(), BorderLayout.NORTH);
                recipeTablePanel.add(recipeTable, BorderLayout.CENTER);

                recipePanel.add(recipeTablePanel, BorderLayout.CENTER);
                recipePanel.add(recipeButtonsPanel, BorderLayout.WEST);

                recipeWindow.pack();
                recipeWindow.setLocationRelativeTo(null);
                recipeWindow.setSize(400, 300);
                recipeWindow.setVisible(true);
            } catch(Exception error) {
                System.out.println("Click a Table Row First!");
            }
        }
    }

    public JTable createRecipeTable(Dish dish) {
        String[] columnNames = {"Ingredient", "Quantity"};
        int columnNamesLength = columnNames.length;

        Object[][] data = retrieveTableData(dish, columnNamesLength);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable recipeTable = new JTable();
        recipeTable.setModel(tableModel);
        return recipeTable;
    }

    public Object[][] retrieveTableData(Dish dish, int columnNamesLength) {

        Object[][] data = new Object[dish.getRecipe().size()][columnNamesLength];
        int counter = 0;

        for (Map.Entry<Ingredient, Number> entry : dish.getRecipe().entrySet()) {
            data[counter][0] = entry.getKey();
            data[counter][1] = entry.getValue();
            tableOfIngredients.add(entry.getKey());
            counter++;
        }

        return data;
    }

    private class AddListener implements ActionListener {

        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{

            if (!quantityField.getText().equals("")) {
                Ingredient ingredient = (Ingredient) ingredientCombo.getSelectedItem();
                if (!tableOfIngredients.contains(ingredient)) {
                    Integer quantity = Integer.valueOf(quantityField.getText());
                    addedIngredients.put(ingredient, quantity);
                }
            }
            recipeWindow.dispose();
        }
    }

    private class DeleteDishListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTable.getSelectedRow();
            try {
                Dish value = dishesList.get(row);
                deletedDishes.add(value);
            } catch(IndexOutOfBoundsException error) {
            }
        }
    }

    public List<Dish> returnDeletedDishes() {
        List<Dish> temp = deletedDishes;
        deletedDishes = new ArrayList<>();
        return temp;
    }

    private class DeleteListener implements ActionListener {

        public void actionPerformed(ActionEvent e) throws IllegalArgumentException{

            int row = recipeTable.getSelectedRow();
            Ingredient ingredient = tableOfIngredients.get(row);
            if (tableOfIngredients.contains(ingredient)) {
                deletedIngredients.put(recipeDish, ingredient);
                recipeWindow.dispose();
            }
        }
    }

    public Map<Dish, Ingredient> returnDeletedIngredients() {
        Map<Dish, Ingredient> temp = deletedIngredients;
        deletedIngredients = new HashMap<>();
        return temp;
    }

    public Map<Dish, Map<Ingredient, Integer>> returnAddedIngredients() {
        Map<Dish, Map<Ingredient, Integer>> temp = new HashMap<>();
        temp.put(recipeDish, addedIngredients);
        addedIngredients = new HashMap<>();
        return temp;
    }

    public List<Object[]> returnAddedDishes() {
        List<Object[]>  temp = addedDishes;
        addedDishes = new ArrayList<>();
        return temp;
    }

    public void refreshDishes(List<Dish> dishes, List<Ingredient> ingredients) {
        this.dishesList = dishes;
        this.ingredientsList = ingredients;

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