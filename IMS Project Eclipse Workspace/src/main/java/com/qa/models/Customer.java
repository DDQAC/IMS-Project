package com.qa.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa.utils.DB;

public class Customer {

	private int customerID;

	private String firstName;

	private String lastName;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String postcode;

	private String email;

	private ArrayList<Item> basket;

	public Customer() {
	}

	public Customer(String firstName, String lastName, String addressLine1, String addressLine2, String city,
			String postcode, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postcode = postcode;
		this.email = email;
	}

	public Customer(int customerID, String firstName, String lastName, String addressLine1, String addressLine2,
			String city, String postcode, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.customerID = customerID;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postcode = postcode;
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [customerID: " + customerID + " | firstName: " + firstName + " | lastName: " + lastName
				+ " | addressLine1: " + addressLine1 + " | addressLine2: " + addressLine2 + " | city: " + city
				+ " | postcode: " + postcode + " | email: " + email + "]";
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Item> getBasket() {
		return basket;
	}

	public void setBasket(ArrayList<Item> basket) {
		this.basket = basket;
	}

	public void viewBasket() {
		ArrayList<Item> basket = this.getBasket();
		if (basket.isEmpty()) {
			System.out.println("Your basket is empty!");
		} else {
			float total = 0.00F;
			for (Item i : basket) {
				float subtotal = i.getPrice() * i.getQuantity();
				System.out.printf("%s Subtotal = £%.2f%n", i, subtotal);
				total += subtotal;
			}
			System.out.printf("Basket total is £%.2f%n", total);
		}
	}

	public void addItemToBasket(int itemID, int quantity) {

		Item addMe = Item.search(itemID);

		int stock = addMe.getQuantity();
		if (stock < quantity) {
			System.out.println("Sorry we only have " + stock + " " + addMe.getName() + " available");
		} else {
			addMe.setQuantity(quantity);
			ArrayList<Item> basket = this.getBasket();
			basket.add(addMe);
			this.setBasket(basket);
		}

	}

	public void removeItemFromBasket(int itemID) {

		ArrayList<Item> basket = this.getBasket();
		Item deleteMe = new Item();
		for (Item i : basket) {
			if (i.getItemID() == itemID) {
				deleteMe = i;
			}
		}
		basket.remove(deleteMe);
	}

	public void clearBasket() {
		ArrayList<Item> emptyBasket = new ArrayList<Item>();
		this.setBasket(emptyBasket);
	}

	public void placeOrder(List<Item> items) {
		if (this.getCustomerID() == 0) {
			System.out.println("Unfortunately this user cannot yet place orders - please register and then try again.");
		} else if (items.isEmpty()) {
			System.out.println("No items have been added to this order!");
		} else {
			Order order = new Order(this, items);
			order.add();
		}

	}

	public void add() {

		String fName = this.getFirstName();
		String lName = this.getLastName();
		String address1 = this.getAddressLine1();
		String address2 = this.getAddressLine2();
		String city = this.getCity();
		String postcode = this.getPostcode();
		String email = this.getEmail();

		String sql = "INSERT INTO customers (first_name, last_name, address_line_1, address_line_2, city, postcode, email) VALUES (\""
				+ fName + "\", \"" + lName + "\", \"" + address1 + "\", \"" + address2 + "\", \"" + city + "\", \""
				+ postcode + "\", \"" + email + "\")";

		String retrieveID = "SELECT CID FROM customers ORDER BY CID DESC LIMIT 1";

		try {
			DB.exUpdate(sql);
			ResultSet rs = DB.exQuery(retrieveID);
			while (rs.next()) {
				this.setCustomerID(rs.getInt("CID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.clearBasket();
	}

	public static Customer search(int custID) {

		int CID = 0;
		String fName = null;
		String lName = null;
		String address1 = null;
		String address2 = null;
		String city = null;
		String postcode = null;
		String email = null;

		String sql = "SELECT * FROM customers WHERE CID = " + custID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				CID = rs.getInt("CID");
				fName = rs.getString("first_name");
				lName = rs.getString("last_name");
				address1 = rs.getString("address_line_1");
				address2 = rs.getString("address_line_2");
				city = rs.getString("city");
				postcode = rs.getString("postcode");
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Customer found = new Customer(CID, fName, lName, address1, address2, city, postcode, email);
		found.clearBasket();
		return found;
	}

	public static void viewAll() {

		String sql = "SELECT * FROM customers";

		System.out.println(
				"Customer ID | First Name | Last Name | Address | Address cont. | City/Town | Postcode | Email");

		try {
			ResultSet results = DB.exQuery(sql);

			while (results.next()) {
				System.out.println(results.getInt("CID") + " | " + results.getString("first_name") + " | "
						+ results.getString("last_name") + " | " + results.getString("address_line_1") + " | "
						+ results.getString("address_line_2") + " | " + results.getString("city") + " | "
						+ results.getString("postcode") + " | " + results.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(int custID) {

		String fName = this.getFirstName();
		String lName = this.getLastName();
		String address1 = this.getAddressLine1();
		String address2 = this.getAddressLine2();
		String city = this.getCity();
		String postcode = this.getPostcode();
		String email = this.getEmail();

		String sql = "UPDATE customers SET first_name = \"" + fName + "\", last_name = \"" + lName
				+ "\", address_line_1 = \"" + address1 + "\", address_line_2 = \"" + address2 + "\", city = \"" + city
				+ "\", postcode = \"" + postcode + "\", email = \"" + email + "\" WHERE CID = " + custID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.clearBasket();
	}

	public static void delete(int custID) {

		String sql = "DELETE FROM customers WHERE CID = " + custID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
