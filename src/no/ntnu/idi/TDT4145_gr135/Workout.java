package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Workout {
	
	public int workoutID;
	public int date; // 21/03/19 --> 210319. workout.dateify(this.date) = 
	public int time;
	public int length;
	public int performance;
	public int personalShape;
	
	public ArrayList<String> dateify(int d) { // ddmmyy --> [dd, mm, yy]
		String date = ""+d;
		ArrayList<String> dates = new ArrayList<>();
		//Year
		String year = date.substring(date.length()-2, date.length());
		
		//Month
		date = date.substring(0,date.length()-2);
		String month = date.substring(date.length()-2, date.length());
		
		//Day
		String day = date.substring(0,date.length()-2);
		
		dates.add(day);
		dates.add(month);
		dates.add(year);
		
		return dates;

	}
	
	private ArrayList<String> timeify(int t) { // hhmm --> [hh, mm]
		String time = ""+t;
		ArrayList<String> times = new ArrayList<>();
		
		String minuite = time.substring(time.length()-2, time.length());
		String hour = time.substring(0,time.length()-2);
				
		times.add(hour);
		times.add(minuite);
		
		return times;
	}

	private Workout() {
		
	}
	
	public String toString() {
		ArrayList<String> date = dateify(this.getDate());
		ArrayList<String> time = timeify(this.getTime());
		
		return "ID:"+ this.getWorkoutID() +
				"\t(" +
				date.get(0)+"/"+
				date.get(1)+"/"+
				date.get(2) +				"\t| "+
				time.get(0)+":"+time.get(1)+"\t| "+
				this.getLength() +			"\t| "+
				this.getPerformance() +		"\t| "+
				this.getPersonalShape()+
				")";
	}
	
	private Workout(int workoutID, int date, int time, int length, int performance, int personalShape) {
		this.workoutID = workoutID;
		this.date = date;
		this.time = time;
		this.length = length;
		this.performance = performance;
		this.personalShape = personalShape;
		
	}
	
	public static Workout insertWorkoutIntoDB(int date, int time, int length, int performance, int personalShape, Connection conn) {
		try {
			int workoutID = 0;
			Workout[] workouts = retrieveWorkoutsFromDB(conn);
			for (Workout workout: workouts) {
				if(workout.getWorkoutID()>workoutID) {
					workoutID = workout.getWorkoutID();
				}
			}
			
			String query = "INSERT INTO workout VALUES(?, ?, ?, ?, ?, ?);";
		
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, workoutID+1);
			statement.setInt(2, date);
			statement.setInt(3, time);
			statement.setInt(4, length);
			statement.setInt(5, performance);
			statement.setInt(6, personalShape);
			statement.execute();
			
			Workout workout = new Workout(workoutID+1,date,time,length,performance,personalShape);
			return workout;
			
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
	
	public static Workout[] retrieveRecentWorkoutsFromDB(Connection conn, int latest_n) {
		try {
			String query = "SELECT TOP "+latest_n+"* FROM workout ORDER BY DATE ASC";
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
			
		} catch (Exception e) {
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
