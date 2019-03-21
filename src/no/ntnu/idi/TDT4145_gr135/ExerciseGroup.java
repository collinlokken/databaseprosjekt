package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExerciseGroup {

    private int excerciseGroupID;
    private String name;
    
	public String toString() {
		return "ExerciseGroup [excerciseGroupID=" + excerciseGroupID + ", name=" + name + "]";
	}

    public ExerciseGroup(int excerciseGroupID, String name) {
        this.excerciseGroupID = excerciseGroupID;
        this.name = name;
    }

    public ExerciseGroup(int excerciseGroupID) {
        this.excerciseGroupID = excerciseGroupID;
    }

    public int getExcerciseGroupID() {
        return excerciseGroupID;
    }

    public void setExcerciseGroupID(int excerciseGroupID) {
        this.excerciseGroupID = excerciseGroupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ExerciseGroup insertExerciseGroupIntoDB( Connection conn, Scanner input) {
        try {
        	
        	System.out.println("Give the exercisegroupe a name...\n");
        	String name = input.next();
        	int excerciseGroupID = 0;
        	ExerciseGroup[] excerciseGroups = retrieveExerciseGroupsFromDB(conn);
			for (ExerciseGroup excerciseGroup: excerciseGroups) {
				if(excerciseGroup.getExcerciseGroupID()>excerciseGroupID) {
					excerciseGroupID = excerciseGroup.getExcerciseGroupID();
				}
			}
            String query = "INSERT INTO excercisegroup VALUES(?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setInt(1, excerciseGroupID+1);
            preparedStatement.setString(2, name);
            preparedStatement.execute();
            
            while(true) {
				System.out.println("Please enter exercise id to this exerciseGroup\nenter 'quit' when you're done\nexercises are:\n");
				Excercise[] excercises = Excercise.retrieveExcercisesFromDB(conn);
				for (Excercise excercise:excercises) {
					System.out.println("ID:"+excercise.getExcerciseID()+"  Name:"+excercise.getName());
				}
				System.out.println("\n");
				
				String excercise = input.next();
				
				if(excercise.equals("quit")) {
					break;
				}
				
				String ce = "INSERT INTO contains_excercise values(?, ?)";
				
				PreparedStatement stmt = conn.prepareStatement(ce);

				stmt.setInt(1, Integer.parseInt(excercise));
				stmt.setInt(2, excerciseGroupID+1);
				stmt.execute();
				System.out.println("nice :)\n");
				
			}
            ExerciseGroup exerciseGroup = new ExerciseGroup(excerciseGroupID, name);
            return exerciseGroup;
            
        } 
        catch (SQLException e) {
            System.out.println("- ERROR -");
            System.out.println(e);
        }
        return null;
    }

	public static ExerciseGroup[] retrieveExerciseGroupsFromDB(Connection conn) {
        try {
            String query = "SELECT * FROM ExcerciseGroup";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<ExerciseGroup> exerciseGroups = new ArrayList<>();

            while(rs.next()) {
                ExerciseGroup exerciseGroup = new ExerciseGroup(rs.getInt("ExcerciseGroupID"), rs.getString("Name"));
                exerciseGroups.add(exerciseGroup);
            }

            return exerciseGroups.toArray(new ExerciseGroup[exerciseGroups.size()]);

        } catch (SQLException e) {
            System.out.println("- ERROR -");
            System.out.println(e);
            return null;
        }
    }

    public static Excercise[] findExercises(Connection conn, Scanner input) {
        try {
        	System.out.println("please specify exerciseGroupID\nexercisegroups are:");
        	
        	ExerciseGroup[] excercisegroups = ExerciseGroup.retrieveExerciseGroupsFromDB(conn);
			for (ExerciseGroup excercisegroup:excercisegroups) {
				System.out.println("ID:"+excercisegroup.getExcerciseGroupID()+"  Name:"+excercisegroup.getName());
			}
			
			int excerciseGroupID = input.nextInt();
        	
            String query = "SELECT excercise.excerciseID, excercice.name, excercise.type FROM excercises NATURAL JOIN contains_excercise NATURAL JOIN excerciseGroup WHERE ExcerciseGroupID=" + excerciseGroupID;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<Excercise> excercises = new ArrayList<>();

            while(rs.next()) {
                Excercise excercise = new Excercise(rs.getInt("excerciseID"), rs.getString("name"), rs.getString("type"));

                excercises.add(excercise);
            }
            return excercises.toArray(new Excercise[excercises.size()]);
        } catch (SQLException e) {
            System.out.println("- ERROR -");
            System.out.println(e);
            return null;
        }
    }
}
