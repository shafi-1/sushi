package comp1206.sushi.server;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import comp1206.sushi.common.*;

/**
 * Provides the Sushi Server user interface
 *
 */
public class ServerWindow extends JFrame implements UpdateListener {

	private static final long serialVersionUID = -4661566573959270000L;
	private ServerInterface server;
	private PostcodesTab postcodesTab;
	private DronesTab dronesTab;
	private StaffTab staffTab;
	private SuppliersTab suppliersTab;
	private IngredientsTab ingredientsTab;
	private DishesTab dishesTab;
	private UsersTab usersTab;
	private OrdersTab ordersTab;

	/**
	 * Create a new server window
	 * @param server instance of server to interact with
	 */
	public ServerWindow(ServerInterface server) {
		super("Sushi Server");
		this.server = server;
		this.setTitle(server.getRestaurantName() + " Server");
		server.addUpdateListener(this);

		JTabbedPane tabbedPane = new JTabbedPane();


		setPostcodesTab(new PostcodesTab(this.server.getPostcodes()));
		setDronesTab(new DronesTab(this.server.getDrones(), server));
		setStaffTab(new StaffTab(this.server.getStaff(), server));
		setSuppliersTab(new SuppliersTab(this.server.getSuppliers(), this.server.getPostcodes()));
		setIngredientsTab(new IngredientsTab(this.server.getIngredients(), this.server.getSuppliers(), server));
		setDishesTab(new DishesTab(this.server.getDishes(), this.server.getIngredients(), server));
		setUsersTab(new UsersTab(this.server.getUsers()));
		setOrdersTab(new OrdersTab(this.server.getOrders(), server));
		tabbedPane.add("Postcodes", getPostcodesTab());
		tabbedPane.add("Drones", getDronesTab());
		tabbedPane.add("Staff", getStaffTab());
		tabbedPane.add("Suppliers", getSuppliersTab());
		tabbedPane.add("Ingredients", getIngredientsTab());
		tabbedPane.add("Dishes", getDishesTab());
		tabbedPane.add("Users", getUsersTab());
		tabbedPane.add("Orders", getOrdersTab());


		super.setContentPane(tabbedPane);

		//Display window
		setSize(800,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//Start timed updates
		startTimer();
	}

	/**
	 * Start the timer which updates the user interface based on the given interval to update all panels
	 */
	public void startTimer() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		int timeInterval = 5;

		scheduler.scheduleAtFixedRate(() -> refreshAll(), 0, timeInterval, TimeUnit.SECONDS);
	}

	/**
	 * Refresh all parts of the server application based on receiving new data, calling the server afresh
	 */
	public void refreshAll() {
		getOrdersTab().refreshOrders(this.server.getOrders());
		getUsersTab().refreshUsers(this.server.getUsers());

		for (Postcode postcode : getPostcodesTab().returnDeletedPostcodes()) {
			boolean inUse = false;
			for (Supplier supplier : this.server.getSuppliers()) {
				if (supplier.getPostcode() == postcode) {
					inUse = true;
					break;
				}
			}
			if (!inUse) {
				for (User user : this.server.getUsers()) {
					if (user.getPostcode() == postcode) {
						inUse = true;
						break;
					}
				}
			}
			if (!inUse) {
				try {
					this.server.removePostcode(postcode);
				} catch (ServerInterface.UnableToDeleteException e) {
					System.out.println(e);
				}
			} else {
				System.out.println("Sorry!");
			}
		}

		for (Supplier supplier : getSuppliersTab().returnDeletedSuppliers()) {
			boolean inUse = false;
			for (Ingredient ingredient : this.server.getIngredients()) {
				if (ingredient.getSupplier() == supplier) {
					inUse = true;
					break;
				}
			}
			if (!inUse) {
				try {
					this.server.removeSupplier(supplier);
				} catch (ServerInterface.UnableToDeleteException e) {
					System.out.println(e);
				}
			} else {
				System.out.println("Sorry!");
			}
		}

		for (Ingredient ingredient : getIngredientsTab().returnDeletedIngredients()) {
			boolean inUse = false;
			for (Dish dish : this.server.getDishes()) {
				for (Map.Entry<Ingredient, Number> entry : dish.getRecipe().entrySet()) {
					if (entry.getKey() == ingredient) {
						inUse = true;
						break;
					}
				}
			}
			if (!inUse) {
				try {
					this.server.removeIngredient(ingredient);
				} catch (ServerInterface.UnableToDeleteException e) {
					System.out.println(e);
				}
			} else {
				System.out.println("Sorry!");
			}
		}

		for (Dish dish : getDishesTab().returnDeletedDishes()) {
			try {
				this.server.removeDish(dish);
			} catch (ServerInterface.UnableToDeleteException e) {
				System.out.println(e);
			}
		}

		for (Drone drone : getDronesTab().returnDeletedDrones()) {
			try {
				this.server.removeDrone(drone);
			} catch (ServerInterface.UnableToDeleteException e) {
				System.out.println(e);
			}
		}

		for (Staff staff : getStaffTab().returnDeletedStaff()) {
			try {
				this.server.removeStaff(staff);
			} catch (ServerInterface.UnableToDeleteException e) {
				System.out.println(e);
			}
		}
		for (Map.Entry<Dish, Map<Ingredient, Integer>> entry : getDishesTab().returnAddedIngredients().entrySet()) {
			Dish dish = entry.getKey();
			for (Map.Entry<Ingredient, Integer> ingredient : entry.getValue().entrySet()) {
				this.server.addIngredientToDish(dish, ingredient.getKey(), ingredient.getValue());
			}
		}
		for (Map.Entry<Dish, Ingredient> entry : getDishesTab().returnDeletedIngredients().entrySet()) {
			this.server.removeIngredientFromDish(entry.getKey(), entry.getValue());
		}

		for (String s : getPostcodesTab().returnAddedPostcodes()) {
			this.server.addPostcode(s);
		}
		for (Integer s : getDronesTab().returnAddedDrones()) {
			this.server.addDrone(s);
		}
		for (String s : getStaffTab().returnAddedStaff()) {
			this.server.addStaff(s);
		}
		for (Map.Entry<String, Postcode> entry : getSuppliersTab().returnAddedSuppliers().entrySet()) {
			this.server.addSupplier(entry.getKey(), entry.getValue());
		}
		for (Object[] o : getIngredientsTab().returnAddedIngredients()) {
			this.server.addIngredient((String)o[0], (String)o[1], (Supplier)o[2], (Integer)o[3], (Integer)o[4]);
		}
		for (Object[] o : getDishesTab().returnAddedDishes()) {
			this.server.addDish((String)o[0], (String)o[1], (Integer)o[2], (Integer)o[3], (Integer)o[4]);
		}
		getPostcodesTab().refreshPostcodes(this.server.getPostcodes());
		getDronesTab().refreshDrones(this.server.getDrones());
		getStaffTab().refreshStaff(this.server.getStaff());
		getSuppliersTab().refreshSuppliers(this.server.getSuppliers(), this.server.getPostcodes());
		getIngredientsTab().refreshIngredients(this.server.getIngredients(), this.server.getSuppliers());
		getDishesTab().refreshDishes(this.server.getDishes(), this.server.getIngredients());
	}

	@Override
	/**
	 * Respond to the model being updated by refreshing all data displays
	 */
	public void updated(UpdateEvent updateEvent) {
		refreshAll();
	}

	public PostcodesTab getPostcodesTab() {
		return postcodesTab;
	}

	public void setPostcodesTab(PostcodesTab postcodesTab) {
		this.postcodesTab = postcodesTab;
	}

	public DronesTab getDronesTab() {
		return dronesTab;
	}

	public void setDronesTab(DronesTab dronesTab) {
		this.dronesTab = dronesTab;
	}

	public StaffTab getStaffTab() {
		return staffTab;
	}

	public void setStaffTab(StaffTab staffTab) {
		this.staffTab = staffTab;
	}

	public SuppliersTab getSuppliersTab() {
		return suppliersTab;
	}

	public void setSuppliersTab(SuppliersTab suppliersTab) {
		this.suppliersTab = suppliersTab;
	}

	public IngredientsTab getIngredientsTab() {
		return ingredientsTab;
	}

	public void setIngredientsTab(IngredientsTab ingredientsTab) {
		this.ingredientsTab = ingredientsTab;
	}

	public DishesTab getDishesTab() {
		return dishesTab;
	}

	public void setDishesTab(DishesTab dishesTab) {
		this.dishesTab = dishesTab;
	}

	public UsersTab getUsersTab() {
		return usersTab;
	}

	public void setUsersTab(UsersTab usersTab) {
		this.usersTab = usersTab;
	}

	public OrdersTab getOrdersTab() {
		return ordersTab;
	}

	public void setOrdersTab(OrdersTab ordersTab) {
		this.ordersTab = ordersTab;
	}

}