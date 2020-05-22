package com.qa.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.qa.utils.DB;

public class Plant extends Item {

	private int plantID;

	private String commonName;

	private String genus;

	private String species;

	private String variegation;

	private String lightReq;

	private String waterReq;

//	private enum LightReq {
//		FULL("Full sun"), INDIRECT("Bright, indirect light"), SHADE("Shade");
//
//		private String description;
//
//		public String getDescription() {
//			return this.description;
//		}
//
//		private LightReq(String description) {
//			this.description = description;
//		}
//	}
//
//	private enum WaterReq {
//		HIGH("High"), MED("Medium"), LOW("Low");
//
//		private String description;
//
//		public String getDescription() {
//			return this.description;
//		}
//
//		private WaterReq(String description) {
//			this.description = description;
//		}
//	}

	public Plant() {
	}

	public Plant(String commonName, String genus, String species, String variegation) {
		this.commonName = commonName;
		this.genus = genus;
		this.species = species;
		this.variegation = variegation;
	}

	public Plant(int plantID, String commonName, String genus, String species, String variegation, String lightReq,
			String waterReq) {
		this.plantID = plantID;
		this.commonName = commonName;
		this.genus = genus;
		this.species = species;
		this.variegation = variegation;
		this.lightReq = lightReq;
		this.waterReq = waterReq;
	}

	public Plant(String commonName, String genus, String species, String variegation, float price, int stock) {
		super(commonName, price, stock);
		this.commonName = commonName;
		this.genus = genus;
		this.species = species;
		this.variegation = variegation;
	}

	public Plant(int plantID, String commonName, String genus, String species, String variegation, String lightReq,
			String waterReq, int itemID, String name, float price, int stock) {
		super(itemID, name, price, stock);
		this.plantID = plantID;
		this.commonName = commonName;
		this.genus = genus;
		this.species = species;
		this.variegation = variegation;
		this.lightReq = lightReq;
		this.waterReq = waterReq;
	}

	@Override
	public String toString() {
		return "Plant [" + super.toString() + " plantID: " + plantID + " | commonName: " + commonName + " | genus: "
				+ genus + " | species: " + species + " | variegation: " + variegation + "]";
	}

	public int getPlantID() {
		return plantID;
	}

	public void setPlantID(int plantID) {
		this.plantID = plantID;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getVariegation() {
		return variegation;
	}

	public void setVariegation(String variegation) {
		this.variegation = variegation;
	}

	public String getLightReq() {
		return lightReq;
	}

	public void setLightReq(String lightReq) {
		this.lightReq = lightReq;
	}

	public String getWaterReq() {
		return waterReq;
	}

	public void setWaterReq(String waterReq) {
		this.waterReq = waterReq;
	}

	@Override
	public void add() {

		String commonName = this.getCommonName();
		String genus = this.getGenus();
		String species = this.getSpecies();
		String var = this.getVariegation();

		String addPlant = "INSERT INTO plants (common_name, genus, species, variegation) VALUES (\"" + commonName
				+ "\", \"" + genus + "\", \"" + species + "\", \"" + var + "\")";

		String retrievePlantID = "SELECT PID FROM plants ORDER BY PID DESC LIMIT 1";

		try {
			DB.exUpdate(addPlant);
			ResultSet rs = DB.exQuery(retrievePlantID);
			while (rs.next()) {
				this.setPlantID(rs.getInt("PID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int plantID = this.getPlantID();
		String itemName = this.getName();
		float price = this.getPrice();
		int quantity = this.getQuantity();

		String addItem = "INSERT INTO items (FK_PID, name, price, stock) VALUES (" + plantID + ", \"" + itemName
				+ "\", " + price + ", " + quantity + ")";

		String retrieveItemID = "SELECT IID FROM items ORDER BY IID DESC LIMIT 1";

		try {
			DB.exUpdate(addItem);
			ResultSet rs = DB.exQuery(retrieveItemID);
			while (rs.next()) {
				this.setItemID(rs.getInt("IID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Plant search(int plantID) {

		int PID = 0;
		String commonName = null;
		String genus = null;
		String species = null;
		String var = null;
		String light = null;
		String water = null;

		String plantSQL = "SELECT * FROM plants WHERE PID = " + plantID;

		try {

			ResultSet rs = DB.exQuery(plantSQL);

			while (rs.next()) {
				PID = rs.getInt("PID");
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

		int IID = 0;
		String itemName = null;
		float price = 0.0F;
		int quantity = 0;

		String itemSQL = "SELECT * FROM items WHERE FK_PID = " + PID + " ORDER BY IID LIMIT 1";

		try {

			ResultSet rs = DB.exQuery(itemSQL);

			while (rs.next()) {
				IID = rs.getInt("IID");
				itemName = rs.getString("name");
				price = rs.getFloat("price");
				quantity = rs.getInt("stock");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new Plant(PID, commonName, genus, species, var, light, water, IID, itemName, price, quantity);
	}

	public static void viewAll() {

		String sql = "SELECT * FROM plants";

		System.out.println(
				"Plant ID | Common Name | Genus | Species | Variegation | Light Requirements | Water Requirements");

		try {
			ResultSet results = DB.exQuery(sql);

			while (results.next()) {
				System.out.println(results.getInt("PID") + " | " + results.getString("common_name") + " | "
						+ results.getString("genus") + " | " + results.getString("species") + " | "
						+ results.getString("variegation") + " | " + results.getString("light_req") + " | "
						+ results.getString("water_req"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(int plantID) {

		String cName = this.getCommonName();
		String genus = this.getGenus();
		String species = this.getSpecies();
		String var = this.getVariegation();

		String sql = "UPDATE plants SET common_name = \"" + cName + "\", genus = \"" + genus + "\", species = \""
				+ species + "\", variegation = \"" + var + "\" WHERE PID = " + plantID;

		super.update(this.getItemID());

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(int plantID) {

		String plantSQL = "DELETE FROM plants WHERE PID = " + plantID;
		String itemSQL = "DELETE FROM items WHERE FK_PID = " + plantID;

		try {
			DB.exUpdate(plantSQL);
			DB.exUpdate(itemSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
