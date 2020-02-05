import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Vika {
	
	//TODO fix the "no suitable driver" bug.

	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:derby:MyDbTest");
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM derbyDB");
			rs.next();
			int count = rs.getInt(1);
			System.out.println("There are " + count + " rows in my table.");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
