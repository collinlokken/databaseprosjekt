package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.Scanner;

/*
@author Christopher Collin Loekken

post-workouts-1-030419-0910-2-5-5
*/

public class Driver {
	
	public static void command(String cmd, Connection conn) {
		String[] ls = cmd.split("-");
		
		String command = ls[0];
		String table = ls[1];
		
		if (table.equals("excercise")) {
			if (command.equals("post")) {
				Excercise excercise = new Excercise(Integer.parseInt(ls[2]), ls[3], ls[4]);
				excercise.insertExcerciseIntoDB(conn, excercise.getExcerciseID(), excercise.getName(), excercise.getType());
			} else if (ls[0].equals("get")) {

			} else {
				throw new IllegalArgumentException("First word must me get or post");
			}

		}
		else if (table.equals("workout")) {
			workout(ls, conn);
		}
		else if (table.equals("equipment")) {
			equipment(ls, conn);

			if (command.equals("get")) {
				
			}
		}
		else if (table.equals("workouts")) {
			if (command.equals("get")) {
				Workout[] workouts = Workout.retrieveWorkoutsFromDB(conn);
				for (Workout workout:workouts) {
					System.out.println(workout);
				}
				
			}
			else if (command.equals("post")) {
				int workoutID = Integer.parseInt(ls[2]);
				int date = Integer.parseInt(ls[3]);
				int time = Integer.parseInt(ls[4]);
				int length = Integer.parseInt(ls[5]);
				int performance = Integer.parseInt(ls[6]);
				int personalShape = Integer.parseInt(ls[7]);
				Workout workout = Workout.insertWorkoutIntoDB(workoutID, date, time, length, performance, personalShape, conn);
				System.out.println("Created : "+workout);
			}
		}
		else if (table.equals("equipment")) {
			if (command.equals("get")) {
				
			}
		}
		
	}
	
	public static void main(String[] args) {
		
		try {
			Scanner input = new Scanner(System.in);
			System.out.println("enter database username");
			String usr = input.next();
			
			System.out.println("enter database password");
			String psw = input.next();
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			String url = "jdbc:mysql://127.0.0.1:3306/";
			Connection conn = DriverManager.getConnection(url+"?user="+usr+"&password="+psw+"&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			
			PreparedStatement ps = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS gruppe135_treningsdatabase");
			int result = ps.executeUpdate();
			if (result==1){
				System.out.println("Database succsessfully created.");
			}
			else {
				System.out.println("An error has occured... please try again.");
			}
			
			Connection conn1 = DriverManager.getConnection(url+"gruppe135_treningsdatabase?user="+usr+"&password="+psw+"&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			final String sql0 = "CREATE TABLE IF NOT EXISTS `workout`( /* class */\r\n" + 
					"`WorkoutID` int(11) not null primary key,\r\n" + 
					"`Date` int(11) not null,\r\n" + 
					"`Time` int(11) not null,\r\n" + 
					"`Length` int(11) not null,\r\n" +
					"`PersonalShape` int(11) not null,\r\n" + 
					"`Performance` int(11) not null\r\n" + 
					")";
			
			final String sql1 = "CREATE TABLE IF NOT EXISTS `excercise`( /* class */\r\n" + 
					"`ExcerciseID` int(11) not null primary key,\r\n" + 
					"`Name` varchar(20) not null,\r\n" + 
					"`Type` varchar(20) not null,\r\n" + 
					"constraint check(`Type`=\"fixed_equipment_excercise\" or `Type`=\"bodyweight_excercise\")\r\n" + 
					")";
			
			final String sql2 = "CREATE TABLE IF NOT EXISTS `excercisegroup`( /* class */\r\n" + 
					"`ExcerciseGroupID` int(11) not null primary key,\r\n" + 
					"`Name` varchar(20) not null\r\n" + 
					")";
			
			final String sql3 = "CREATE TABLE IF NOT EXISTS `contains_excercisegruoup`( /* junction table */\r\n" + 
					"`WorkoutID` int(11) not null,\r\n" + 
					"`ExcerciseGroupID` int(11) not null,\r\n" + 
					"primary key (`WorkoutID`,`ExcerciseGroupID`),\r\n" + 
					"constraint foreign key(`WorkoutID`) references `workout`(`WorkoutID`)on delete cascade on update cascade,\r\n" + 
					"constraint foreign key(`ExcerciseGroupID`) references `excercisegroup`(`ExcerciseGroupID`) on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql4 = "CREATE TABLE IF NOT EXISTS `contains_excercise`( /* junction table */\r\n" + 
					"`ExcerciseID` int(11) not null,\r\n" + 
					"`ExcerciseGroupID` int(11) not null,\r\n" + 
					"primary key(`ExcerciseID`,`ExcerciseGroupID`),\r\n" + 
					"constraint foreign key(`ExcerciseID`) references `excercise`(`ExcerciseID`)on delete cascade on update cascade,\r\n" + 
					"constraint foreign key(`ExcerciseGroupID`) references `excercisegroup`(`ExcerciseGroupID`)on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql5 = "CREATE TABLE IF NOT EXISTS `contains_excercises`( /* junction table */\r\n" + 
					"`WorkoutID` int(11) not null,\r\n" + 
					"`ExcerciseID` int(11) not null,\r\n" + 
					"primary key(`WorkoutID`,`ExcerciseID`),\r\n" + 
					"constraint foreign key(`WorkoutID`) references `workout`(`WorkoutID`)on delete cascade on update cascade,\r\n" + 
					"constraint foreign key(`ExcerciseID`) references `excercise`(`ExcerciseID`)on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql6 = "CREATE TABLE IF NOT EXISTS `fixed_equipment_excercise`( /* subclass - references primary key */\r\n" + 
					"`FEEID` int(11) not null,\r\n" + 
					"`ExcerciseID` int(11) not null,\r\n" + 
					"`Weight` int(4) not null,\r\n" + 
					"`Sets` int(3) not null,\r\n" + 
					"primary key(`FEEID`,`ExcerciseID`),\r\n" + 
					"constraint foreign key(`ExcerciseID`) references `excercise`(`ExcerciseID`)on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql7 = "CREATE TABLE IF NOT EXISTS `bodyweight_excercise`( /* subclass - references primary key */\r\n" + 
					"`BEID` int(11) not null,\r\n" + 
					"`ExcerciseID` int(11) not null,\r\n" + 
					"`Description` varchar(100) default null,\r\n" + 
					"primary key(`BEID`,`ExcerciseID`),\r\n" + 
					"constraint foreign key(`ExcerciseID`) references `excercise`(`ExcerciseID`)on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql8 = "CREATE TABLE IF NOT EXISTS `equipment`( /* class */\r\n" + 
					"`EquipmentID` int(11) not null primary key,\r\n" + 
					"`Name` varchar(20) not null,\r\n" + 
					"`Description` varchar(100) not null\r\n" + 
					")";
			
			final String sql9 = "CREATE TABLE IF NOT EXISTS `uses_equipment`( /* junction table */\r\n" + 
					"`FixedEquipmentExerciseID` int(11) not null,\r\n" + 
					"`EquipmentID` int(11) not null,\r\n" + 
					"primary key(`FixedEquipmentExerciseID`,`EquipmentID`),\r\n" + 
					"constraint foreign key(`FixedEquipmentExerciseID`) references `fixed_equipment_excercise`(`ExcerciseID`)on delete cascade on update cascade,\r\n" + 
					"constraint foreign key(`EquipmentID`) references `equipment`(`EquipmentID`)on delete cascade on update cascade\r\n" + 
					")";
			
			final String sql10 = "CREATE TABLE IF NOT EXISTS `note`( /* week class*/\r\n" + 
					"`NoteID` int(11) not null,\r\n" + 
					"`WorkoutID` int(11) not null references `workout`(`WorkoutID`) on delete cascade on update cascade,\r\n" + 
					"`Description` varchar(100) not null,\r\n" + 
					"primary key(`NoteID`,`WorkoutID`)\r\n" + 
					")";

			Statement stmt = conn1.createStatement();
			
			stmt.execute(sql0);
			stmt.execute(sql1);
			stmt.execute(sql2);
			stmt.execute(sql3);
			stmt.execute(sql4);
			stmt.execute(sql5);
			stmt.execute(sql6);
			stmt.execute(sql7);
			stmt.execute(sql8);
			stmt.execute(sql9);
			stmt.execute(sql10);
			
			
			System.out.println("Tables succsessfully created.");
			
			while (true) {
				
				System.out.println("enter command");
				String cmd = input.next();
				command(cmd, conn1);
				
				
			}
			
		}
		catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException exc){
			exc.printStackTrace();
		}

	}

}
