package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.Scanner;

/*
@author Christopher Collin Loekken

post-workout-030419-0910-2-5-5
*/

public class Driver {
	
	public static Boolean validate(String[] cmd) {
		Boolean valid = false;
		
		if(		cmd[0].equals("get") &&
				(cmd.length==2||cmd[2].equals("time")&&cmd.length==3)) {
			valid = true;
		}
		else if(cmd[0].equals("post") && cmd.length==2 &&
				(
				cmd[1].equals("exercise")
				||
				cmd[1].equals("equipment")
				||
				cmd[1].equals("workout")
				||
				cmd[1].equals("exercisegroup")
				)){
			valid = true;
			
		}
		
		return valid;
	}
	
	public static String[] commandify(String cmd) {
		
		String[] command = cmd.split("-");
		
		return command;
	}
		
	public static void command(String cmd, Connection conn, Scanner input) {
		
		String[] ls = commandify(cmd);
		Boolean validCommand = validate(ls);
		
		
		if(validCommand) {
			
		
		
			String command = ls[0];
			String table = ls[1];
			if (table.equals("exercisegroup")) {
				
				if (command.equals("get")) {
					if (ls.length==3) {
						Excercise[] excercises = ExerciseGroup.findExercises(conn, input);
						for(Excercise excercise: excercises) {
							System.out.println(excercise);
							
						}
						return;
					}
					else{
						ExerciseGroup[] exerciseGroups = ExerciseGroup.retrieveExerciseGroupsFromDB(conn);
						for (ExerciseGroup exerciseGroup : exerciseGroups) {
							System.out.println(exerciseGroup);
							
						}
						return;
					}
				}
				else if(command.equals("post")) {
					ExerciseGroup exercisegroup = ExerciseGroup.insertExerciseGroupIntoDB(conn, input);
					System.out.println("Created:" + exercisegroup);
					return;
				}
			}
			
			if (table.equals("exercise")) {
				
				if (command.equals("post")) {
					Excercise excercise = Excercise.insertExcerciseIntoDB(conn, input);
					System.out.println("Created:" + excercise);
					return;
					
				} else if (command.equals("get")) {
					if(ls.length==3) {
						if(ls[2].equals("time")) {
							Excercise[] excercises = Excercise.retrieveExcercisesFromDate(conn, input);
							for (Excercise excercise:excercises) {
								System.out.println(excercise);
								
							}
							return;
						}
					}
					else {
						Excercise[] excercises = Excercise.retrieveExcercisesFromDB(conn);
						for (Excercise excercise:excercises) {
							System.out.println(excercise);
						}
						return;
					}
				}
	
			}
			else if (table.equals("equipment")) {
				if (command.equals("post")) {
					
					Equipment equipment = Equipment.insertEquipmentIntoDB(conn, input);
					System.out.println(equipment);
					return;
				}
	
				else if (command.equals("get")) {
					Equipment[] equipments = Equipment.retrieveEquipmentsFromDB(conn);
					for (Equipment equipment: equipments) {
						System.out.println(equipment);
					}
					return;
				}
				
			}
			else if (table.equals("workout")) {
				
				if (command.equals("post")) {
										
					Workout workout = Workout.insertWorkoutIntoDB(conn, input);
					System.out.println("Created : "+workout);
					return;
				}
				else if (command.equals("get")) {
					Workout[] workouts = Workout.retrieveRecentWorkoutsFromDB(conn, input);
					for (Workout workout:workouts) {
						System.out.println(workout);
					}
					return;
					
				}
			}
			else {
				System.out.println("Table name is not correct");
			}
		}
		else {
			System.out.println("Thats not a valid command!");
		}
	}
	
	public static void main(String[] args) {
		
		try {
			Connection conn = null;
			String url = "jdbc:mysql://127.0.0.1:3306/";
			String usr = "";
			String psw = "";
			Scanner input = new Scanner(System.in);
			while(true) {
				try {
					
					System.out.println("enter database username");
					usr = input.next();
					
					System.out.println("enter database password");
					psw = input.next();
					
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
										
					conn = DriverManager.getConnection(url+"?user="+usr+"&password="+psw+"&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
					
					break;
				} catch (SQLException e) {
					System.out.println("\nConnection failed...\nCheck if username and password are correct\nYou must also be running a local instance of a MYSQL 80 database\n");
				}
			}
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
					"`Name` varchar(30) not null,\r\n" + 
					"`Type` varchar(30) not null,\r\n" + 
					"constraint check(`Type`=\"fixed_equipment_excercise\" or `Type`=\"bodyweight_excercise\")\r\n" + 
					")";
			
			final String sql2 = "CREATE TABLE IF NOT EXISTS `excercisegroup`( /* class */\r\n" + 
					"`ExcerciseGroupID` int(11) not null primary key,\r\n" + 
					"`Name` varchar(30) not null\r\n" + 
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
					"`Name` varchar(30) not null,\r\n" + 
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
			stmt.execute(sql4);
			stmt.execute(sql5);
			stmt.execute(sql6);
			stmt.execute(sql7);
			stmt.execute(sql8);
			stmt.execute(sql9);
			stmt.execute(sql10);
			
			
			System.out.println("Tables succsessfully created.");
			System.out.println("\n(input 'help' for help)\n");
			
			while (true) {
				
				System.out.println("\nenter command \n");
				String cmd = input.next();
				if (cmd.equals("help")) {
					String s = "TO INSERT:\n\t";
					s += "post-<tablename>\n\n";
					s += "TO RETRIEVE:\n\t";
					s += "get-<tablename>\n\n";
					s += "TABLES:\n\t";
					s += "exercise\n\t"
							+ "workout\n\t"
							+ "equipment\n\t"
							+ "exercisegroup\n\n";
					s += "TO GET exercises in timeintervall:\n\t"
							+ "get-exercise-time";
					System.out.println(s);
				}
				else {
					command(cmd, conn1, input);
				}
				
			}
			
		}
		catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException exc){
			exc.printStackTrace();
		}

	}

}

/*
* THANOS CAR
* THANOS CAR
 */
