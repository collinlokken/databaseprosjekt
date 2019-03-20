package no.ntnu.idi.TDT4145_gr135;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Workout {
	try {
		
		if (ls[0].equals("get")) {
			String query = "SELECT * FROM " + ls[1];
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			String result = "";
			String header = "| " + ls[1] + " |\n";
			while(rs.next()) {
				result += header;
				result += "| Workout";
				result += rs.getInt("WorkoutID")+	 " | ";
				result += rs.getInt("Date")+		 " | ";
				result += rs.getInt("Time")+		 " | ";
				result += rs.getInt("Length")+		 " | ";
				result += rs.getString("InfoAboutExcercises")+ " | ";
				result += rs.getInt("PersonalShape")+ "| ";
				result += rs.getInt("Performance")+  " | ";
			}
			result += "\n";
			return result;
		}
		else if (ls[0].equals("post")){
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO " + ls[1] + " VALUES(?, ?, ?, ?, ?, ?, ?)"
			);
			
			stmt.setInt(1, Integer.parseInt(ls[2])); //id
			stmt.setInt(2, Integer.parseInt(ls[3])); //date
			stmt.setInt(3, Integer.parseInt(ls[4])); //time
			stmt.setInt(4, Integer.parseInt(ls[5])); //length
			stmt.setString(5, ls[6]);				 //Info
			stmt.setInt(6, Integer.parseInt(ls[7])); //personal shape
			stmt.setInt(7, Integer.parseInt(ls[8])); //performance
			stmt.execute();
			return "Values successfully inserted";
		}
		
	} catch (SQLException | ArrayIndexOutOfBoundsException e) {
		String result  = "\n- ERROR -";
		result += e.getMessage();
		return result;
	}
	return "\n";
}
}
