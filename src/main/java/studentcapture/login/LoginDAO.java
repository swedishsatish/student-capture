package studentcapture.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import studentcapture.user.User;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Repository
public class LoginDAO {

    private final int GET_USER_BY_USERNAME = 0;
    private final int GET_USER_BY_ID = 1;


    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new user to the User-table in the database.
     *
     * @param user  instance that contains information of the user to be added.
     */
    public ErrorFlags addUser(User user) {

        if(userNameExist(user.getUserName())) {
            return ErrorFlags.USEREXISTS;
        }

        //check if email exists
        if(emailExists(user.getEmail())) {
            return ErrorFlags.EMAILEXISTS;
        }

        String sql = "INSERT INTO users"
                + " (username, firstname, lastname, email, pswd)"
                + " VALUES (?, ?, ?, ?, ?)";
        //Preparing statement
        Object[] args = new Object[] {user.getUserName(),user.getFirstName(),
                                      user.getLastName(),user.getEmail(),
                                      user.getPswd()};

        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.VARCHAR,Types.VARCHAR};
        //Execute
        try {
            jdbcTemplate.update(sql, args,types);
        } catch (DataIntegrityViolationException e) {
			return ErrorFlags.USEREXISTS;
		}

		return ErrorFlags.NOERROR;
    }

    private boolean emailExists(String email) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  email = ?)";

        Object[] args = {email};
        int[] types = {Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,Boolean.class);
    }


    /**
     * Get user by username.
     * @param value to be searched for in respect to  given flag
     * @param flag 0 returns user object by giving username
     *             1 returns user object by giving userID.
     *
     *
     * @return User object, that contains all related information to
     *          a user otherwise null will be returned.
     */
    public User getUser(String value, int flag) {

        String sql=null;
        Object[] args;
        int[] types;

        if(flag == GET_USER_BY_USERNAME) {
            args = new Object[]{value};
            types = new int[]{Types.VARCHAR};
            sql = "SELECT  * FROM users WHERE username = ?";
        } else if(flag == GET_USER_BY_ID) {
            args = new Object[]{Integer.parseInt(value)};
            types = new int[]{Types.INTEGER};
            sql = "SELECT  * FROM users WHERE userid = ?";
        } else {
            //Invalid flag
            return null;
        }

        User user = null;
        try {
            user = (User) jdbcTemplate.queryForObject(sql, args,types,
                    new UserWrapper());
        } catch (Exception e) {
            return  null;
        }

        return user;
    }

    /**
     * Updates user with username from user object.
     *
     * @param user
     * @return true if update was successfull else false
     */
    public boolean updateUser(User user) {

        if(!userNameExist(user.getUserName())) {
            return false;
        }

        String sql = "UPDATE users SET firstname = ?, lastname = ?, email = ?," +
                " pswd = ?, token = ? WHERE username = ?";


        Object[] args = {user.getFirstName(),user.getLastName(),user.getEmail(),
                         user.getPswd(),user.getToken(),user.getUserName()};
        int[] types = {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                       Types.VARCHAR,Types.VARCHAR};

        try{
            jdbcTemplate.update(sql, args,types);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Checks if given username already exists.
     *
     * @param userName user name for user.
     * @return true if it exists else false
     */
    public boolean userNameExist(String userName) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  UserName = ?)";

        Object[] args = new Object[]{userName};
        int[] types = new int[]{Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,Boolean.class);
    }

    /**
     * Return email for a user
     *
     * @param userID
     * @return
     */
    public String getEmail(int userID) {
        String sql = "SELECT Email FROM users WHERE userID = ?";

        try {
            return jdbcTemplate.queryForObject(sql,new Object[]{userID},String.class);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Change a user's email address
     * @param userID Identifier of the user to modify
     * @param email The new email address
     */
    public boolean setEmail(int userID, String email) {
        String sql = "UPDATE Users SET Email = ? WHERE UserID = ?";
        try {
            jdbcTemplate.update(sql, email, userID);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static Integer getUserIdFromSession(HttpSession session) {
    	return Integer.parseInt(session.getAttribute("userid").toString());
    }


	/**
     *  Used to collect user information, and return a hashmap.
     *  @author Timmy Olsson
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User(rs.getString("username"),rs.getString("firstname"),
                                 rs.getString("lastname"),rs.getString("email"),
                                 rs.getString("pswd"));

            user.setUserID(rs.getString("userid"));
            user.setToken(rs.getString("token"));

            return user;
        }
    }
}

