package no.ntnu.idi.TDT4145_gr135;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseGroup {

    private int excerciseGroupID;
    private String name;

    public ExerciseGroup(int excerciseGroupID, String name) {
        this.excerciseGroupID = excerciseGroupID;
        this.name = name;
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

    public ResultSet insertExerciseGroupIntoDB( Connection conn, int excerciseGroupID, String name) {
        try {
            String query = "INSERT INTO excercise VALUES(?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);

            preparedStatement.setInt(1, excerciseGroupID);
            preparedStatement.setString(2, name);
        } catch (SQLException e) {
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
}