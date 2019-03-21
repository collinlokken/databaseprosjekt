package no.ntnu.idi.TDT4145_gr135;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContainsExercise {
    private int exerciseID;
    private int exerciseGroupID;

    public ContainsExercise(int exerciseID, int exerciseGroupID) {
        this.exerciseID = exerciseID;
        this.exerciseGroupID = exerciseGroupID;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getExerciseGroupID() {
        return exerciseGroupID;
    }

    public void setExerciseGroupID(int exerciseGroupID) {
        this.exerciseGroupID = exerciseGroupID;
    }

    public ResultSet addExerciseToGroup(Connection conn, int excerciseID, int excerciseGroupID) {
        try {
            String query = "INSERT INTO contains_excercise VALUES(?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);

            preparedStatement.setInt(1, excerciseID);
            preparedStatement.setInt(2, excerciseGroupID);

            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("- ERROR -");
            System.out.println(e);
        }
        return null;
    }
}