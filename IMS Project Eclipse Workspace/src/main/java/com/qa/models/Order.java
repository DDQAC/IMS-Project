package com.qa.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.qa.utils.DB;

public class Order {

	private int orderID;

	private Customer customer;

	private List<Item> items;

	private Timestamp placed;

	public Order() {
	}

	public Order(Customer customer, List<Item> items) {
		this.customer = customer;
		this.items = items;
	}

	public Order(int orderID, Customer customer, List<Item> items, Timestamp placed) {
		this.orderID = orderID;
		this.customer = customer;
		this.items = items;
		this.placed = placed;
	}

	@Override
	public String toString() {
		return "Order [orderID: " + orderID + " | placed: " + placed + " | total(): " + total() + "\ncustomer: "
				+ customer + "\nitems: " + items + "]";
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Timestamp getPlaced() {
		return placed;
	}

	public void setPlaced(Timestamp placed) {
		this.placed = placed;
	}

	public void add() {

		int custID = this.getCustomer().getCustomerID();

		String sql = "INSERT INTO orders (FK_CID) VALUES (" + custID + ")";

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String retrieveNewOrder = "SELECT OID, placed FROM orders ORDER BY OID DESC LIMIT 1";

		try {

			ResultSet rs = DB.exQuery(retrieveNewOrder);

			while (rs.next()) {
				this.setOrderID(rs.getInt("OID"));
				this.setPlaced(rs.getTimestamp("placed"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int orderID = this.getOrderID();

		for (Item orderedItem : this.getItems()) {

			int itemID = orderedItem.getItemID();
			int quantityOrdered = orderedItem.getQuantity();

			Item stockUpdate = Item.search(itemID);
			int oldStock = stockUpdate.getQuantity();

			if (!stockUpdate.checkQuantity(quantityOrdered)) {
				if (quantityOrdered == 1) {
					System.out.println(
							"Sorry, the item '" + orderedItem.getName() + "' (ID = " + itemID + ") is out of stock!");
					continue;
				} else {
					System.out.println("Sorry, you have ordered more of the item '" + orderedItem.getName() + "' (ID = "
							+ itemID + ") than we have available!");
					continue;
				}
			}

			int newStock = oldStock - quantityOrdered;
			stockUpdate.setQuantity(newStock);
			stockUpdate.update(stockUpdate.getItemID());

			String insertIntoOrders_items = "INSERT INTO orders_items (FK_OID, FK_IID, item_quantity) VALUES ("
					+ orderID + ", " + itemID + ", " + quantityOrdered + ")";

			try {
				DB.exUpdate(insertIntoOrders_items);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public static Order search(int orderID) {

		int OID = 0;
		int CID = 0;
		Timestamp placed = null;

		String sql = "SELECT * FROM orders WHERE OID = " + orderID;

		try {
			ResultSet rs = DB.exQuery(sql);

			while (rs.next()) {
				OID = rs.getInt("OID");
				CID = rs.getInt("FK_CID");
				placed = rs.getTimestamp("placed");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Customer cust = Customer.search(CID);

		List<Item> items = new ArrayList<Item>();
		ArrayList<int[]> itemIDs = new ArrayList<int[]>();

		sql = "SELECT FK_IID, item_quantity FROM orders_items WHERE FK_OID = " + OID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				int IID = rs.getInt("FK_IID");
				int quantity = rs.getInt("item_quantity");
				int[] itemData = new int[2];
				itemData[0] = IID;
				itemData[1] = quantity;
				itemIDs.add(itemData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int[] data : itemIDs) {
			int IID = data[0];
			int quantity = data[1];
			Item item = Item.search(IID);
			item.setQuantity(quantity);
			items.add(item);
		}

		return new Order(OID, cust, items, placed);

	}

	public void update(int orderID) {

		Order oldOrder = Order.search(orderID);

		int newCustID = this.getCustomer().getCustomerID();
		Timestamp newPlaced = this.getPlaced();

		if (newCustID != 0 && newPlaced != null) {
			if (newCustID != oldOrder.getCustomer().getCustomerID() || newPlaced != oldOrder.getPlaced()) {
				String sql = "UPDATE orders SET FK_CID = " + newCustID + ", placed = \"" + newPlaced + "\" WHERE OID = "
						+ orderID;

				try {
					DB.exUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		for (Item oldItem : oldOrder.getItems()) {

			int oldQuantity = oldItem.getQuantity();

			Item restock = Item.search(oldItem.getItemID());

			int oldStock = restock.getQuantity();
			int newStock = oldStock + oldQuantity;

			restock.setQuantity(newStock);
			restock.update(restock.getItemID());
		}

		String rmvOldOrders_items = "DELETE FROM orders_items WHERE FK_OID = " + orderID;

		try {
			DB.exUpdate(rmvOldOrders_items);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Item newItem : this.getItems()) {

			int itemID = newItem.getItemID();
			int quantityOrdered = newItem.getQuantity();

			Item stockUpdate = Item.search(itemID);
			int oldStock = stockUpdate.getQuantity();

			if (!stockUpdate.checkQuantity(quantityOrdered)) {
				if (quantityOrdered == 1) {
					System.out.println(
							"Sorry, the item '" + newItem.getName() + "' (ID = " + itemID + ") is out of stock!");
					continue;
				} else {
					System.out.println("Sorry, you have ordered more of the item '" + newItem.getName() + "' (ID = "
							+ itemID + ") than we have available!");
					continue;
				}
			}

			int newStock = oldStock - quantityOrdered;
			stockUpdate.setQuantity(newStock);
			stockUpdate.update(stockUpdate.getItemID());

			String insertNewOrders_items = "INSERT INTO orders_items (FK_OID, FK_IID, item_quantity) VALUES (" + orderID
					+ ", " + itemID + ", " + quantityOrdered + ")";

			try {
				DB.exUpdate(insertNewOrders_items);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void delete(int orderID) {

		String ordersSQL = "DELETE FROM orders WHERE OID = " + orderID;
		String orders_itemsSQL = "DELETE FROM orders_items WHERE FK_OID = " + orderID;

		Order deleteMe = Order.search(orderID);
		for (Item i : deleteMe.getItems()) {

			int quantityOrdered = i.getQuantity();

			Item restock = Item.search(i.getItemID());

			int oldStock = restock.getQuantity();
			int newStock = oldStock + quantityOrdered;

			restock.setQuantity(newStock);
			restock.update(restock.getItemID());
		}

		try {
			DB.exUpdate(ordersSQL);
			DB.exUpdate(orders_itemsSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static float total(int orderID) {
		float total = 0.0F;

		String sql = "SELECT * FROM order_sum WHERE order_ID = " + orderID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				total = rs.getFloat("order_total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

	public float total() {

		int orderID = this.getOrderID();
		float total = 0.0F;

		String sql = "SELECT * FROM order_sum WHERE order_ID = " + orderID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				total = rs.getFloat("order_total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

}
