package com.qa.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.qa.utils.DB;

public class Item {

	private int itemID;

	private String name;

	private float price;

	private int quantity;

	public Item() {
	}

	public Item(String name, float price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public Item(int itemID, String name, float price, int quantity) {
		this.itemID = itemID;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Item [itemID: " + itemID + " | name: " + name + " | price: " + price + " | quantity: " + quantity + "]";
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int IID) {
		itemID = IID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean checkQuantity() {
		if (getQuantity() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkQuantity(int howMany) {
		if (getQuantity() >= howMany) {
			return true;
		} else {
			return false;
		}
	}

	public void add() {

		String name = this.getName();
		float price = this.getPrice();
		int quantity = this.getQuantity();

		String sql = "INSERT INTO items (name, price, stock) VALUES (" + name + "\", " + price + ", " + quantity + ")";

		String retrieveID = "SELECT IID FROM items ORDER BY IID DESC LIMIT 1";

		try {
			DB.exUpdate(sql);
			ResultSet rs = DB.exQuery(retrieveID);
			while (rs.next()) {
				this.setItemID(rs.getInt("IID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Item search(int itemID) {

		int IID = 0;
		int PID = 0;
		String name = null;
		float price = 0.0F;
		int quantity = 0;

		String sql = "SELECT * FROM items WHERE IID = " + itemID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				IID = rs.getInt("IID");
				PID = rs.getInt("FK_PID");
				name = rs.getString("name");
				price = rs.getFloat("price");
				quantity = rs.getInt("stock");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (PID == 0) {

			return new Item(IID, name, price, quantity);

		} else {

			String commonName = null;
			String genus = null;
			String species = null;
			String var = null;
			String light = null;
			String water = null;

			String plantSQL = "SELECT * FROM plants WHERE PID = " + PID;

			try {

				ResultSet rs = DB.exQuery(plantSQL);

				while (rs.next()) {
					commonName = rs.getString("common_name");
					genus = rs.getString("genus");
					species = rs.getString("species");
					var = rs.getString("Variegation");
					light = rs.getString("light_req");
					water = rs.getString("water_req");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return new Plant(PID, commonName, genus, species, var, light, water, IID, name, price, quantity);

		}

	}

	public static void viewAll() {

		String sql = "SELECT * FROM items";

		System.out.println("Item ID | Plant ID | Name | Price | Stock Availability");

		try {
			ResultSet results = DB.exQuery(sql);

			while (results.next()) {
				System.out.println(
						results.getInt("IID") + " | " + results.getInt("FK_PID") + " | " + results.getString("name")
								+ " | " + results.getFloat("price") + " | " + results.getInt("stock"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void update(int itemID) {

		String name = this.getName();
		float price = this.getPrice();
		int quantity = this.getQuantity();

		String sql = "UPDATE items SET name = \"" + name + "\", price = " + price + ", stock = " + quantity
				+ " WHERE IID = " + itemID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(int itemID) {

		String sql = "DELETE FROM items WHERE IID = " + itemID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
