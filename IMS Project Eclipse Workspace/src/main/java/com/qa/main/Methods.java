package com.qa.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.qa.models.Customer;
import com.qa.models.Item;
import com.qa.models.Plant;
import com.qa.models.User;
import com.qa.utils.DB;
import com.qa.utils.Scan;

public class Methods {

	public static int choose() {

		String input = Scan.input();
		int choice = 0;
		if (input.matches("\\d")) {
			choice = Integer.parseInt(input);
		} else {
			choice = -1;
		}
		return choice;
	}

	public static User newUser(User user) {
		System.out.println("Registering new user...");
		System.out.println("Please enter your new username:");
		user.setUsername(Scan.input());
		System.out.println("Please enter your new password:");
		user.setPassword(Scan.input());
		user.registerNewUser();
		return user;
	}

	public static Customer inputCustomer() {

		Customer newCust = new Customer();
		System.out.println("Please enter your first name:");
		newCust.setFirstName(Scan.input());
		System.out.println("Please enter your last name:");
		newCust.setLastName(Scan.input());
		System.out.println("Please enter your address...");
		System.out.println("First line:");
		newCust.setAddressLine1(Scan.input());
		System.out.println("Second line (Optional - press Enter to skip):");
		newCust.setAddressLine2(Scan.input());
		System.out.println("City:");
		newCust.setCity(Scan.input());
		System.out.println("Postcode:");
		newCust.setPostcode(Scan.input());
		System.out.println("Please enter your email:");
		newCust.setEmail(Scan.input());
		return newCust;
	}

	public static Item inputItem() {
		Item newItem = new Item();
		System.out.println("Enter the item name:");
		newItem.setName(Scan.input());
		System.out.println("Enter the price:");
		newItem.setPrice(Float.parseFloat(Scan.input()));
		System.out.println("Enter the stock availability:");
		newItem.setQuantity(choose());
		System.out.println("Is this item correct? (y/n)" + newItem);
		return newItem;
	}

	public static Plant inputPlant() {
		Plant newPlant = new Plant();
		System.out.println("Enter the common name:");
		newPlant.setName(Scan.input());
		System.out.println("Enter the genus:");
		newPlant.setGenus(Scan.input());
		System.out.println("Enter the species:");
		newPlant.setSpecies(Scan.input());
		System.out.println("Enter the variegation: (Optional - press Enter to skip)");
		newPlant.setVariegation(Scan.input());
		System.out.println("Enter the price:");
		newPlant.setPrice(Float.parseFloat(Scan.input()));
		System.out.println("Enter the stock availability:");
		newPlant.setQuantity(choose());
		System.out.println("Is this plant correct? (y/n) " + newPlant);
		return newPlant;
	}

	public static ArrayList<Integer> getIDs(char type) {
		String table = null;
		switch (type) { // Could this be a dictionary implementation?
		case 'C':
			table = "customers";
			break;
		case 'I':
			table = "items";
			break;
		case 'O':
			table = "orders";
			break;
		case 'P':
			table = "plants";
			break;
		case 'U':
			table = "users";
			break;
		}
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		String sql = "SELECT " + type + "ID FROM " + table;
		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				int ID = rs.getInt(type + "ID");
				IDs.add(ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return IDs;
	}

	public static void browse() {

		ArrayList<Integer> IDs = getIDs('I');
		for (int ID : IDs) {
			System.out.println(Item.search(ID));
		}
	}

	public static void changeUsername(User user) {
		System.out.println("Please enter a new username:");
		String newUsername = Scan.input();
		user.setUsername(newUsername);
		user.update(user.getUserID());
		System.out.println("Username updated to " + newUsername);
	}

	public static void changePassword(User user) {
		System.out.println("Please enter a new password:");
		String newPassword = Scan.input();
		user.setPassword(newPassword);
		user.update(user.getUserID());
		System.out.println("Password updated to " + newPassword);
	}

}
