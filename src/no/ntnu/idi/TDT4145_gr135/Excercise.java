package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Excercise {
	private int excerciseID;
	private String name;
	private String type;
	
	
	public String toString() {
		return "Excercise [excerciseID=" + excerciseID + ", name=" + name + ", type=" + type + "]";
	}
	
	public Excercise() {
		
	}
	
	public Excercise(int excerciseID, String name, String type) {
		this.excerciseID = excerciseID;
		this.name = name;
		this.type = type;
	}

	public int getExcerciseID() {
		return excerciseID;
	}

	public void setExcerciseID(int excerciseID) {
		this.excerciseID = excerciseID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static Excercise insertExcerciseIntoDB(Connection conn, Scanner input) {
		try {
			String query = "INSERT INTO excercise VALUES(?, ?, ?)";
			
			System.out.println("Exercice name...");
			String name = input.next();
			
			System.out.println("Exercise type... (either fixed_equipment_excercise or bodyweight_excercise)");
			String type = input.next();
			
			if (!(type.equals("fixed_equipment_excercise") || type.equals("bodyweight_excercise"))) {
				
				while(true) {
					System.out.println("Hey! I told you either fixed_equipment_excercise or bodyweight_excercise!");
					type = input.next();
					if (type.equals("fixed_equipment_excercise") || type.equals("bodyweight_excercise")) {
						break;
					}
				}
			}
				
			Excercise[] excercises = Excercise.retrieveExcercisesFromDB(conn);
			int excerciseid = 0;
			for(Excercise excercise:excercises) {
				if(excercise.getExcerciseID()>excerciseid) {
					excerciseid = excercise.getExcerciseID();
				}
			}
			
			PreparedStatement preparedStatement = conn.prepareStatement(query);

			preparedStatement.setInt(1, excerciseid+1);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, type);
			preparedStatement.execute();
			
			if (type.equals("fixed_equipment_excercise")) {
				System.out.println("weight:");
				int weight = input.nextInt();
				System.out.println("sets:");
				int sets = input.nextInt();
				
				String fee = "INSERT INTO fixed_equipment_excercise VALUES(?,?,?,?)";
				
								
				
				PreparedStatement feestmt = conn.prepareStatement(fee);
				feestmt.setInt(1, excerciseid+1);
				feestmt.setInt(2, excerciseid+1);
				feestmt.setInt(3, weight);
				feestmt.setInt(4, sets);
				
				feestmt.execute();
				
				System.out.println("please specify equipment id for fixed_equipment_excercise\nequipments are:\n");
				Equipment[] equipments = Equipment.retrieveEquipmentsFromDB(conn);
				for (Equipment equipment:equipments) {
					System.out.println("ID:"+equipment.getEquipmentID()+"  Name:"+equipment.getName());
				}
				String equipment = input.next();
				
				String ue = "INSERT INTO contains_excercise values(?, ?)";
				
				PreparedStatement stmt = conn.prepareStatement(ue);
				stmt.setInt(1, excerciseid+1);
				stmt.setInt(1, Integer.parseInt(equipment));
			}
			else if (type.equals("bodyweight_excercise")) {
				System.out.println("bodyweight_excercise description...");
				String description = input.next();
				
				
				String fee = "INSERT INTO bodyweight_excercise VALUES(?,?,?)";
				
								
				
				PreparedStatement feestmt = conn.prepareStatement(fee);
				feestmt.setInt(1, excerciseid+1);
				feestmt.setInt(2, excerciseid+1);
				feestmt.setString(3, description);
				
				feestmt.execute();
			}
			Excercise excercise = new Excercise(excerciseid+1, name, type);
			return excercise;
			
		} catch (SQLException | ArrayIndexOutOfBoundsException e) {
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static Excercise[] retrieveExcercisesFromDB(Connection conn) {
		try {
			String query = "SELECT * FROM excercise";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);

			List<Excercise> excercises = new ArrayList<>();
			while(rs.next()) {
				Excercise excercise = new Excercise(rs.getInt("ExcerciseID"), rs.getString("name"), rs.getString("type"));

				excercises.add(excercise);
			}

			return excercises.toArray(new Excercise[excercises.size()]);
		}
		catch(SQLException e){
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			return null;
		}
	}
	public static Excercise[] retrieveExcercisesFromDate(Connection conn, Scanner input) {
		try {
			System.out.println("Enter year to include from (number):");
			int year_from = Integer.parseInt(input.next());
			System.out.println("Enter month to include from(number):");
			int month_from = Integer.parseInt(input.next());
			System.out.println("Enter day to include from (number):");
			int day_from = Integer.parseInt(input.next());

			String query = "SELECT * FROM excercise join contains_excercises as ce on excercise.excerciseID = ce.excerciseID" +
					" join workout on ce.workoutID = workout.workoutID where workout.date > " + year_from+month_from+day_from;
			
			Statement statement = conn.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			List<Excercise> excercises = new ArrayList<>();
			while(rs.next()) {
				Excercise excercise = new Excercise();
				excercise.setExcerciseID(rs.getInt("excerciseID"));
				excercise.setName(rs.getString("name"));
				excercise.setType(rs.getString("type"));
				excercises.add(excercise);
			}
			return excercises.toArray(new Excercise[excercises.size()]);
		}
		catch(SQLException e){
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			return null;
		}
	
	}
}
