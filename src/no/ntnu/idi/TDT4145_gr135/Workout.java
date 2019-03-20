package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Workout {
	
	public Integer workoutID;
	public Integer date;
	public Integer time;
	public Integer length;
	public Integer performance;
	public Integer personalShape;
	
	private Workout() {
		
	}
	
	public String toString() {
		String date = ""+this.getDate();
		String time = ""+this.getTime();
		String day = date.substring(0,1);
		String month = date.substring(1,3);
		String year = date.substring(3,5);
		String hour = time.substring(0,1);
		String minuite = time.substring(1,3);
		return "ID:"+ this.getWorkoutID() + "\t(" + day+"/"+month+"/"+year+"/" +"\t| "+hour+":"+minuite +"\t| "+ this.getLength() +"\t| "+ this.getPerformance() +"\t| "+this.getPersonalShape()+")";
	}
	
	private Workout(int workoutID, int date, int time, int length, int performance, int personalShape) {
		this.workoutID = workoutID;
		this.date = date;
		this.time = time;
		this.length = length;
		this.performance = performance;
		this.personalShape = personalShape;
		
	}
	
	public static Workout insertWorkoutIntoDB(int workoutID, int date, int time, int length, int performance, int personalShape, Connection conn) {
		try {
			
			String query = "INSERT INTO workout VALUES(?, ?, ?, ?, ?, ?);";
		
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, workoutID);
			statement.setInt(2, date);
			statement.setInt(3, time);
			statement.setInt(4, length);
			statement.setInt(5, performance);
			statement.setInt(6, personalShape);
			statement.execute();
			
			Workout result = new Workout(workoutID,date,time,length,performance,personalShape);
			return result;
			
		} catch (SQLException e) {
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			
		}
		return null;		
	}
	
	public static Workout[] retrieveWorkoutsFromDB(Connection conn) {
		try {	
			String query = "SELECT * FROM workout";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			List<Workout> workouts = new ArrayList<>();
			while(rs.next()) {
				Workout workout = new Workout();
				workout.setWorkoutID(rs.getInt("workoutID"));
				workout.setDate(rs.getInt("date"));
				workout.setTime(rs.getInt("time"));
				workout.setLength(rs.getInt("length"));
				workout.setPerformance(rs.getInt("performance"));
				workout.setPersonalShape(rs.getInt("personalShape"));
				
				workouts.add(workout);
			}
			
			return workouts.toArray(new Workout[workouts.size()]);
		}
		catch(SQLException e){
			System.out.println("- ERROR -");
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int getWorkoutID() {
		return workoutID;
	}

	public void setWorkoutID(int workoutID) {
		this.workoutID = workoutID;
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
			
}
