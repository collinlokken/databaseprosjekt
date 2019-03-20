package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;

public class Workout {
	
	public int excerciseID;
	public int date;
	public int time;
	public int length;
	public String infoAboutExcercise;
	public int performance;
	public int personalShape;
	
	private Workout(int excerciseID, int date, int time, int length, String info, int performance, int personalShape) {
		this.excerciseID = excerciseID;
		this.date = date;
		this.time = time;
		this.length = length;
		this.infoAboutExcercise = info;
		this.performance = performance;
		this.personalShape = personalShape;
		
	}
	
	public ResultSet insertWorkoutIntoDB(int excerciseID, int date, int time, int length, String info, int performance, int personalShape, Connection conn) {
		try {
			String query = "INSERT INTO workout"+
					""+
					""+
					""+
					"";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
		} catch (SQLException e) {
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			
		}
		return null;		
	}
	
	public ResultSet retrieveWorkoutFromDB(Connection conn) {
		try {	
			String query = "SELECT * FROM workout";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			return rs;
		}
		catch(SQLException e){
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int getExcerciseID() {
		return excerciseID;
	}

	public void setExcerciseID(int excerciseID) {
		this.excerciseID = excerciseID;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getInfoAboutExcercise() {
		return infoAboutExcercise;
	}

	public void setInfoAboutExcercise(String infoAboutExcercise) {
		this.infoAboutExcercise = infoAboutExcercise;
	}

	public int getPerformance() {
		return performance;
	}

	public void setPerformance(int performance) {
		this.performance = performance;
	}

	public int getPersonalShape() {
		return personalShape;
	}

	public void setPersonalShape(int personalShape) {
		this.personalShape = personalShape;
	}
		
		
		
		
//		try {
//	
//			if (ls[0].equals("get")) {
//				
//				
//			}
//			else if (ls[0].equals("post")){
//				PreparedStatement stmt = conn.prepareStatement(
//						"INSERT INTO " + ls[1] + " VALUES(?, ?, ?)"
//				);
//				stmt.setInt(1, Integer.parseInt(ls[2]));
//				stmt.setString(2, ls[3]);
//				stmt.setString(3, ls[4]);
//				stmt.execute();
//				return "Values successfully inserted";
//			}
//		}
//		catch (SQLException | ArrayIndexOutOfBoundsException e) {
//			String result  = "\n- ERROR -";
//			result += e.getMessage();
//			return result;
//		}
//		return "\n";
	
	
}
