
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInterface {
	private Connection conn = null;
	
	private final static String newUserStatement = "INSERT INTO Users "
			+ "(FirstName,Lastname,Personnummer,CasID,Password) VALUES "
			+ "(?,?,?,?,?)";
	private final static String getUserFullNameStatement = "SELECT FirstName,"
			+ "LastName FROM Users WHERE (UserID=?)";
	private final static String getUserStatement = "SELECT * Users WHERE "
			+ "(CasID=?)";
	
	public DatabaseInterface(String path, String user, String password) throws 
			SQLException, ClassNotFoundException {
		
		Class.forName("org.postgresql.Driver");
			
		connect(path, user, password);
	}
	
	protected void finalize() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Close failed
		}
		conn = null;
	}
	
	private void connect(String path, String user, String password) throws SQLException {
		try {
			conn.close();
		} catch (SQLException | NullPointerException e) {
			// TODO Auto-generated catch block
		}
		
		conn = DriverManager.getConnection(path, user, password);
		conn.setAutoCommit(true); // May be reconcidered.
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Close failed
		}
	}
	
	private PreparedStatement createStatement(String query) {
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Create statement fail
			e.printStackTrace();
		}
		
		return stmt;
	}
	
	
	public boolean addUser(String firstName, String lastName, String persNum, 
			String casID, String password) {		
		boolean result = false;
		
		PreparedStatement stmt = createStatement(newUserStatement);
		
		try {
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setString(3, persNum);
			stmt.setString(4, casID);
			stmt.setString(5, password);
			
			stmt.executeUpdate();
			
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getUserFullName(int userID) {
		PreparedStatement stmt = createStatement(getUserFullNameStatement);
		
		try {
			stmt.setInt(1, userID);
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String firstName = rs.getString("FirstName");
			String lastName = rs.getString("LastName");
			
			return firstName + " " + lastName;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Object> getUser(String CasID) {
		List<Object> result = new ArrayList<>();
		
		PreparedStatement stmt = createStatement(getUserStatement);
		
		try {
			stmt.setString(1, CasID);
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			int userID = rs.getInt("UserID");
			String firstName = rs.getString("FirstName");
			String lastName = rs.getString("LastName");
			String persNum = rs.getString("FirstName");
			String password = rs.getString("Password");
			
			result.add(userID);
			result.add(firstName);
			result.add(lastName);
			result.add(persNum);
			result.add(password);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
