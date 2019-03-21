package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Workout {
	
	public int workoutID;
	public int date;
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
	
	public static Workout insertWorkoutIntoDB(Connection conn, Scanner input) {
		try {
			int workoutID = 0;
			Workout[] workouts = retrieveWorkoutsFromDB(conn);
			for (Workout workout: workouts) {
				if(workout.getWorkoutID()>workoutID) {
					workoutID = workout.getWorkoutID();
				}
			}
			String[] dates;
			String[] times;
			while(true) {
				System.out.println("Date: (yy/mm/dd)");
				String d = input.next();
				System.out.println("Time: (hh:mm)");
				String t = input.next();
				dates = d.split("/");
				times = t.split(":");
				if(dates.length==3&&times.length==2) {
					break;
				}
				System.out.println("\nwrong date or time format... try again\n");
			}
			int date = 10000*Integer.parseInt(dates[0]) + 100*Integer.parseInt(dates[1]) + Integer.parseInt(dates[2]);
			int time = 100*Integer.parseInt(times[0]) + Integer.parseInt(times[1]);
			
			System.out.println("length: (1-10)");
			int length = input.nextInt();
			
			System.out.println("perfonrmance: (1-10)");
			int performance= input.nextInt();
			
			System.out.println("personalShape: (1-10)");
			int personalShape= input.nextInt();
			
			String query = "INSERT INTO workout VALUES(?, ?, ?, ?, ?, ?);";
		
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, workoutID+1);
			statement.setInt(2, date);
			statement.setInt(3, time);
			statement.setInt(4, length);
			statement.setInt(5, performance);
			statement.setInt(6, personalShape);
			statement.execute();
			
			while(true) {
				System.out.println("Please enter excercise id to this workout\nenter 'quit' when you're done\nexcercises are:\n");
				Excercise[] excercises = Excercise.retrieveExcercisesFromDB(conn);
				for (Excercise excercise:excercises) {
					System.out.println("ID:"+excercise.getExcerciseID()+"  Name:"+excercise.getName());
				}
				System.out.println("\n");
				
				String excercise = input.next();
				
				if(excercise.equals("quit")) {
					break;
				}
				
				String ce = "INSERT INTO contains_excercises values(?, ?)";
				
				PreparedStatement stmt = conn.prepareStatement(ce);
				stmt.setInt(1, workoutID+1);
				stmt.setInt(2, Integer.parseInt(excercise));
				stmt.execute();
				System.out.println("nice :)\n");
				
			}
			
			
			
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
	
	public static Workout[] retrieveRecentWorkoutsFromDB(Connection conn, Scanner input) {
		try {
			
			System.out.println("Enter a number of workouts to retrieve");
			int latest_n = input.nextInt();
			
			String query = "SELECT * FROM workout ORDER BY DATE ASC LIMIT "+latest_n+";";
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
