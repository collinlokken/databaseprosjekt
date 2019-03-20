package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Equipment {
	
	public int equipmentID;
	public String name;
	public String description;
	
	public String toString() {
		
		return "ID:"+this.getEquipmentID()+"\t("+this.getName()+ "\t| " + this.getDescription()+ "\t) ";
	}
	
	public Equipment() {
		
	}
	
	public Equipment(int equipmentID, String name, String description) {
		this.equipmentID = equipmentID;
		this.name = name;
		this.description = description;
	}
	
	public static Equipment insertEquipmentIntoDB(int equipmentId, String name, String description, Connection conn) {
		try {
			String query = "INSERT INTO equipment VALUES(?, ?, ?);";
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, equipmentId);
			statement.setString(2, name);
			statement.setString(3, description);
			
			statement.execute();
			
			Equipment equipment = new Equipment(equipmentId, name, description);
			
			return equipment;
			
		} catch (SQLException e) {
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static Equipment[] retrieveEquipmentsFromDB(Connection conn) {
		try {
			String query = "SELECT * FROM equipment";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			List<Equipment> equipments = new ArrayList<>();
			
			while(rs.next()) {
				Equipment equipment = new Equipment();
				equipment.setEquipmentID(rs.getInt("equipmentID"));
				equipment.setName(rs.getString("name"));
				equipment.setDescription(rs.getString("description"));
				
				equipments.add(equipment);
			}
			
			return equipments.toArray(new Equipment[equipments.size()]);
			
		} catch (SQLException e) {
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int getEquipmentID() {
		return equipmentID;
	}

	public void setEquipmentID(int equipmentID) {
		this.equipmentID = equipmentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
