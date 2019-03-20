package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Excercise {
	private int excerciseID;
	private String name;
	private String type;

	public Excercise(int excerciseID, String name, String type) {
		excerciseID = excerciseID;
		this.name = name;
		this.type = type;
	}

	public int getExcerciseID() {
		return excerciseID;
	}

	public void setExcerciseID(int excerciseID) {
		excerciseID = excerciseID;
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

	public ResultSet insertExcerciseIntoDB(Connection conn, int excerciseID, String name, String type) {
		try {
			String query = "INSERT INTO excercise VALUES(?, ?, ?)";

			PreparedStatement preparedStatement = conn.prepareStatement(query);

			preparedStatement.setInt(1, excerciseID);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, type);

			//Statement statement = conn.createStatement();
			//ResultSet rs = statement.executeQuery(query);
			return null;
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
}
