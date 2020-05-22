package com.qa.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa.models.Customer;
import com.qa.models.Item;
import com.qa.models.Order;
import com.qa.models.Plant;
import com.qa.models.User;
import com.qa.utils.DB;
import com.qa.utils.Scan;

public class App {

	public static void main(String[] args) throws SQLException {

		try {
			DB.connect();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		User user = new User();

		do {

			System.out.println("New to [insert company name]? Enter 1 to register");
			System.out.println("Already have an account? Enter 2 to log in");
			int choice = Methods.choose();

			if (choice == 1) {

				user = Methods.newUser(user);

			} else if (choice == 2) {

				System.out.println("Please log in...");
				System.out.println("Please enter your username:");
				user.setUsername(Scan.input());
				System.out.println("Please enter your password:");
				user.setPassword(Scan.input());

			} else {

				System.out.println("Please enter a valid choice!");

			}

		} while (!user.login());

		user = User.search(user.getUsername());

		int menuOptions = 0;

		if (user.getUsername().equals("root")) {
			menuOptions = 1;
		} else if (user.getCustomerProfile().getCustomerID() == 0) {
			menuOptions = 2;
		} else {
			menuOptions = 3;
		}

		while (menuOptions != 0) {

			switch (menuOptions) {

			case 1: // Root access menu
				System.out.println(
						"What would you like to do? (Please enter the number which corresponds to your chosen option)");
				System.out.println("Users - 1");
				System.out.println("Customers - 2");
				System.out.println("Orders - 3");
				System.out.println("Items - 4");
				System.out.println("Plants - 5");
				System.out.println("Exit - 0");

				switch (Methods.choose()) {

				case 0: // Exit
					menuOptions = 0;
					System.out.println("Thank you, come again soon!");
					break;

				case 1: // Users
					System.out.println("Create User - 1");
					System.out.println("View Users - 2");
					System.out.println("Update User - 3");
					System.out.println("Delete User - 4");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // Create
						User newUser = new User();
						newUser = Methods.newUser(newUser);
						break;
					case 2: // Read
						ArrayList<Integer> userIDs = Methods.getIDs('U');
						for (int ID : userIDs) {
							System.out.println(User.search(ID));
						}
						break;
					case 3: // Update
						System.out.println("Enter ID of user to update:");
						int updUserID = Methods.choose();
						User updUser = User.search(updUserID);
						System.out.println("Enter new username:");
						updUser.setUsername(Scan.input());
						System.out.println("Enter new password:");
						updUser.setPassword(Scan.input());
						System.out.println("Are you sure you want to change " + User.search(updUserID) + " to "
								+ updUser + "? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							updUser.update(updUserID);
							System.out.println("User successfully updated!");
						}
						break;
					case 4: // Delete
						System.out.println("Enter ID of user to delete:");
						int delUserID = Methods.choose();
						System.out.println("Are you sure you want to delete " + User.search(delUserID) + " (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							User.delete(delUserID);
							System.out.println("User deleted!");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				case 2: // Customers
					System.out.println("Create Customer - 1");
					System.out.println("View Customers - 2");
					System.out.println("Update Customer - 3");
					System.out.println("Delete Customer - 4");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // Create
						Customer newCust = Methods.inputCustomer();
						System.out.println("Are you sure you want to add the following customer? (y/n)");
						System.out.println(newCust);
						if (Scan.input().equalsIgnoreCase("y")) {
							newCust.add();
							System.out.println("Customer added successfully!");
						}
						break;
					case 2: // Read
						ArrayList<Integer> custIDs = Methods.getIDs('C');
						for (int ID : custIDs) {
							System.out.println(Customer.search(ID));
						}
						break;
					case 3: // Update
						System.out.println("Enter ID of customer to update:");
						int updCustID = Methods.choose();
						Customer updCust = Methods.inputCustomer();
						System.out.println("Are you sure you want to change " + Customer.search(updCustID) + " to "
								+ updCust + "? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							updCust.update(updCustID);
							System.out.println("Customer successfully updated!");
						}
						break;
					case 4: // Delete
						System.out.println("Enter ID of customer to delete:");
						int delCustID = Methods.choose();
						System.out.println("Are you sure you want to delete " + Customer.search(delCustID) + " (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							Customer.delete(delCustID);
							System.out.println("Customer deleted!");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				case 3: // Orders
					System.out.println("Create Order - 1");
					System.out.println("View Orders - 2");
					System.out.println("Update Order - 3");
					System.out.println("Delete Order - 4");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // Create
						System.out.println("Enter ID of customer making this order:");
						int custID = Methods.choose();
						Customer cust = Customer.search(custID);
						cust.clearBasket();
						boolean moreItems = false;
						do {
							System.out.println("Enter ID of item to order:");
							int itemID = Methods.choose();
							System.out.println("Enter how many " + Item.search(itemID).getName() + " to order:");
							int quantity = Methods.choose();
							cust.addItemToBasket(itemID, quantity);
							System.out.println("Do you want to continue adding items to this order? (y/n)");
							if (Scan.input().equalsIgnoreCase("y")) {
								moreItems = true;
							} else {
								moreItems = false;
							}
						} while (moreItems);
						System.out.println("Do you want to place the order? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							cust.placeOrder(cust.getBasket());
							System.out.println("Order placed successfully!");
						} else {
							System.out.println("Order not placed!");
						}
						break;
					case 2: // Read
						ArrayList<Integer> ordIDs = Methods.getIDs('O');
						for (int ordID : ordIDs) {
							Order order = Order.search(ordID);
							System.out.println(order);
						}
						break;
					case 3: // Update
						System.out.println("Enter ID of order to update:");
						int updOrderID = Methods.choose();
						Order updOrder = Order.search(updOrderID);
						boolean cont = false;
						do {
							System.out.println("Change customer who made this order - 1");
							System.out.println("Change order contents - 2");
							System.out.println("Back - 0");
							switch (Methods.choose()) {
							case 0: // Back
								cont = false;
								break;
							case 1: // Update customer
								System.out.println("Enter the ID of the Customer to assign this order to:");
								int updCustID = Methods.choose();
								Customer updCust = Customer.search(updCustID);
								System.out.println(
										"Are you sure you want to reassign this order to " + updCust + "? (y/n)");
								if (Scan.input().equalsIgnoreCase("y")) {
									updOrder.setCustomer(updCust);
									updOrder.update(updOrderID);
									System.out.println("Customer successfully updated!");
								}
								cont = true;
								break;
							case 2: // Update Items
								boolean moreUpdItems = false;
								do {
									List<Item> updItems = updOrder.getItems();
									for (Item i : updItems) {
										System.out.println(i);
									}
									System.out.println("Add Item - 1");
									System.out.println("Remove Item - 2");
									System.out.println("Change Quantity - 3");
									System.out.println("Back - 0");
									switch (Methods.choose()) {
									case 0: // Back
										moreUpdItems = false;
										break;
									case 1: // Add Item
										System.out.println("Enter ID of Item to Add:");
										int newItemID = Methods.choose();
										Item newItem = Item.search(newItemID);
										System.out.println("How many " + newItem.getName() + "?:");
										int quantity = Methods.choose();
										newItem.setQuantity(quantity);
										updItems.add(newItem);
										updOrder.setItems(updItems);
										updOrder.update(updOrderID);
										System.out.println("Added item successfully!");
										moreUpdItems = true;
										break;
									case 2: // Remove Item
										System.out.println("Enter ID of Item to Remove:");
										int rmvItemID = Methods.choose();
										Item deleteMe = new Item();
										for (Item i : updItems) {
											if (i.getItemID() == rmvItemID) {
												deleteMe = i;
											}
										}
										updItems.remove(deleteMe);
										updOrder.setItems(updItems);
										updOrder.update(updOrderID);
										System.out.println("Removed item successfully!");
										moreUpdItems = true;
										break;
									case 3: // Change Quantity
										System.out.println("Enter ID of Item to change quantity of:");
										int updItemID = Methods.choose();
										System.out.println("Enter quantity to change to:");
										int updQuantity = Methods.choose();
										for (Item i : updItems) {
											if (i.getItemID() == updItemID) {
												i.setQuantity(updQuantity);
											}
										}
										updOrder.setItems(updItems);
										updOrder.update(updOrderID);
										System.out.println("Changed quantity successfully!");
										moreUpdItems = true;
										break;
									}
								} while (moreUpdItems);
								cont = true;
								break;
							default:
								System.out.println("Please enter a valid option!");
								cont = true;
								break;
							}
						} while (cont);
						break;
					case 4: // Delete
						System.out.println("Enter ID of order to delete:");
						int delOrderID = Methods.choose();
						System.out.println("Are you sure you want to delete " + Order.search(delOrderID) + " (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							Order.delete(delOrderID);
							System.out.println("Order deleted!");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				case 4: // Items
					System.out.println("Create Item - 1");
					System.out.println("View Items - 2");
					System.out.println("Update Item - 3");
					System.out.println("Delete Item - 4");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // Create
						Item newItem = Methods.inputItem();
						if (Scan.input().equalsIgnoreCase("y")) {
							newItem.add();
							System.out.println("Item added successfully!");
						}
						break;
					case 2: // Read
						Methods.browse();
						break;
					case 3: // Update
						System.out.println("Enter ID of Item to update:");
						int updItemID = Methods.choose();
						Item updItem = Methods.inputItem();
						if (Scan.input().equalsIgnoreCase("y")) {
							updItem.update(updItemID);
							System.out.println("Item updated successfully!");
						}
						break;
					case 4: // Delete
						System.out.println("Enter ID of Item to delete:");
						int delItemID = Methods.choose();
						System.out.println("Are you sure you want to delete " + Item.search(delItemID) + " (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							Item.delete(delItemID);
							System.out.println("Item deleted!");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				case 5: // Plants
					System.out.println("Create Plant - 1");
					System.out.println("View Plants - 2");
					System.out.println("Update Plant - 3");
					System.out.println("Delete Plant - 4");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // Create
						Plant newPlant = Methods.inputPlant();
						if (Scan.input().equalsIgnoreCase("y")) {
							newPlant.add();
							System.out.println("Plant added successfully!");
						}
						break;
					case 2: // Read
						ArrayList<Integer> IDs = Methods.getIDs('P');
						for (int ID : IDs) {
							System.out.println(Plant.search(ID));
						}
						break;
					case 3: // Update
						System.out.println("Enter ID of Plant to update:");
						int updPlantID = Methods.choose();
						Plant updPlant = Methods.inputPlant();
						if (Scan.input().equalsIgnoreCase("y")) {
							updPlant.update(updPlantID);
							System.out.println("Plant updated successfully!");
						}
						break;
					case 4: // Delete
						System.out.println("Enter ID of Plant to delete:");
						int delPlantID = Methods.choose();
						System.out.println("Are you sure you want to delete " + Plant.search(delPlantID) + " (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							Plant.delete(delPlantID);
							System.out.println("Plant deleted!");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				default:
					System.out.println("Please enter a valid option!");
					break;
				}

				break;

			case 2: // User not registered as a customer
				System.out.println(
						"What would you like to do? (Please enter the number which corresponds to your chosen option)");
				System.out.println("Customer Registration - 1");
				System.out.println("Browse Catalogue - 2");
				System.out.println("My Account - 3");
				System.out.println("Exit - 0");

				switch (Methods.choose()) {

				case 0: // Exit
					menuOptions = 0;
					System.out.println("Thank you, come again soon!");
					break;

				case 1: // Customer Registration
					System.out.println("Are you sure you want to register as a new customer? (y/n)");
					if (Scan.input().equalsIgnoreCase("y")) {
						Customer newCust = Methods.inputCustomer();
						System.out.println("Are these customer details correct? (y/n)");
						System.out.println(newCust);
						if (Scan.input().equalsIgnoreCase("y")) {
							user.registerAsCustomer(newCust);
							System.out.println("Customer registered successfuly!");
							menuOptions = 3;
						} else {
							System.out.println("Customer not registered: details have been discarded");
						}
					}
					break;

				case 2: // Browse Catalogue
					Methods.browse();
					break;

				case 3: // Account Options
					System.out.println("View Profile - 1");
					System.out.println("Change Username - 2");
					System.out.println("Change Password - 3");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // View Profile
						System.out.println(User.search(user.getUsername()));
						break;
					case 2: // Change Username
						Methods.changeUsername(user);
						break;
					case 3: // Change Password
						Methods.changePassword(user);
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				default:
					System.out.println("Please enter a valid option!");
					break;
				}

				break;

			case 3: // Registered customer
				System.out.println(
						"What would you like to do? (Please enter the number which corresponds to your chosen option)");
				System.out.println("Browse Catalogue - 1");
				System.out.println("Basket - 2");
				System.out.println("My Account - 3");
				System.out.println("Exit - 0");

				switch (Methods.choose()) {

				case 0: // Exit
					menuOptions = 0;
					System.out.println("Thank you, come again soon!");
					break;

				case 1: // Browse Catalogue
					Methods.browse();
					break;

				case 2: // Basket Options
					System.out.println("View Basket - 1");
					System.out.println("Add Item - 2");
					System.out.println("Remove Item - 3");
					System.out.println("Change Quantity - 4");
					System.out.println("Checkout - 5");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // View Basket
						user.getCustomerProfile().viewBasket();
						break;
					case 2: // Add item to Basket
						Methods.browse();
						System.out.println("Please enter the ID of the item you would like to add:");
						int addItemID = Methods.choose();
						Item item = Item.search(addItemID);
						System.out.println("How many " + item.getName() + " would you like to add? ("
								+ item.getQuantity() + " available)");
						int quantity = Methods.choose();
						System.out.println(quantity + " " + item.getName() + " (Subtotal = £"
								+ quantity * item.getPrice() + ") will be added to your basket");
						System.out.println("Is this correct? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							user.getCustomerProfile().addItemToBasket(addItemID, quantity);
							System.out.println("Item added successfully!");
						} else {
							System.out.println("Item has not been added!");
						}
						break;
					case 3: // Remove item from Basket
						user.getCustomerProfile().viewBasket();
						System.out.println("Please enter the ID of the item you would like to remove:");
						int rmvItemID = Methods.choose();
						System.out.println("Are you sure you want to remove " + Item.search(rmvItemID).getName()
								+ " from your basket? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							user.getCustomerProfile().removeItemFromBasket(rmvItemID);
							System.out.println("Item successfully removed!");
						} else {
							System.out.println("Item has not been removed!");
						}
						break;
					case 4: // Change quantity of item in basket
						user.getCustomerProfile().viewBasket();
						System.out.println("Please enter the ID of the item whose quantity you want to change:");
						int updItemID = Methods.choose();
						System.out.println("What would you like to change the quantity to?");
						int newQuantity = Methods.choose();
						System.out.println("Are you sure you want to update the quantity of "
								+ Item.search(updItemID).getName() + " to " + newQuantity + "? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							user.getCustomerProfile().removeItemFromBasket(updItemID);
							user.getCustomerProfile().addItemToBasket(updItemID, newQuantity);
							System.out.println("Quantity has been changed successfully!");
						} else {
							System.out.println("Quantity has not been changed");
						}
						break;
					case 5: // Checkout
						user.getCustomerProfile().viewBasket();
						System.out.println(
								"Are you sure you want to check out? (y/n) An order will be placed for the items in your basket...");
						if (Scan.input().equalsIgnoreCase("y")) {
							ArrayList<Item> basket = user.getCustomerProfile().getBasket();
							user.getCustomerProfile().placeOrder(basket);
							user.getCustomerProfile().clearBasket();
							System.out.println(
									"Your order has been placed! (Go to My Account > My Orders to view processed orders)");
						} else {
							System.out.println("Your order has not yet been placed, feel free to continue shopping");
						}
						break;
					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				case 3: // Account Options
					System.out.println("My Orders - 1");
					System.out.println("View Profile - 2");
					System.out.println("Change Username - 3");
					System.out.println("Change Password - 4");
					System.out.println("Update Profile - 5");
					System.out.println("Back - 0");
					switch (Methods.choose()) {
					case 0: // Back
						break;
					case 1: // View Orders
						List<Integer> IDs = new ArrayList<Integer>();
						String sql = "SELECT OID FROM orders WHERE FK_CID = "
								+ user.getCustomerProfile().getCustomerID();
						try {
							ResultSet rs = DB.exQuery(sql);
							if (!rs.isBeforeFirst()) {
								System.out.println("Your basket is empty!");
								break;
							}
							while (rs.next()) {
								int ID = rs.getInt("OID");
								IDs.add(ID);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						for (int ID : IDs) {
							System.out.println(Order.search(ID));
							System.out.println("Order Total = £" + Order.total(ID));
						}
						break;
					case 2: // View Profile
						System.out.println(User.search(user.getUsername()));
						break;
					case 3: // Change Username
						Methods.changeUsername(user);
						break;
					case 4: // Change Password
						Methods.changePassword(user);
						break;
					case 5: // Update Customer Profile
						System.out.println("Are you sure you wish to update your details? (y/n)");
						if (Scan.input().equalsIgnoreCase("y")) {
							Customer customerUpdate = Methods.inputCustomer();
							System.out.println("New details: " + customerUpdate);
							System.out.println("Are these new details correct? (y/n)");
							if (Scan.input().equalsIgnoreCase("y")) {
								customerUpdate.update(user.getCustomerProfile().getCustomerID());
								user.setCustomerProfile(customerUpdate);
								System.out.println("Customer profile successfully updated!");
							} else {
								System.out.println("Customer profile has not been updated!");
							}
						}
						break;

					default:
						System.out.println("Please enter a valid option!");
						break;
					}
					break;

				default:
					System.out.println("Please enter a valid option!");
					break;
				}

				break;

			default:
				System.out.println("Unknown user!");
				menuOptions = 0;
				break;
			}

		}

		DB.disconnect();

	}

}
