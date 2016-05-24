package studentcapture.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import studentcapture.login.ErrorFlags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Repository
public class UserDAO {

    // This template should be used to send queries to the database
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Add a new user to the User-table in the database.
     * @author Timmy Olsson, c12ton
     *
     * @param user  instance that contains information of the user to be added.
     * @return If an error has occurred a appropriate flag will be returned,
     * else a no error flag
     */
    public ErrorFlags addUser(User user) {

        if(!user.areUserParamsValid()){
            return ErrorFlags.USERCONTAINNULL;
        }

        if(userNameExist(user.getUserName())) {
            return ErrorFlags.USEREXISTS;
        }

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

    /**
     * Get user by username.
     * @author Timmy Olsson, c12ton
     * @param value to be searched for in respect to  given flag
     * @param flag 0 returns user object by giving username
     *             1 returns user object by giving userID.
     *
     *
     * @return User object, that contains all related information to
     *          a user otherwise null will be returned.
     */
    public User getUser(String value, int flag) {

        String sql;
        Object[] args;
        int[] types;

        int GET_USER_BY_ID = 1;
        int GET_USER_BY_USERNAME = 0;

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

        User user;
        try {
            user = (User) jdbcTemplate.queryForObject(sql, args,types,
                    new UserWrapper());
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    /**
     * Updates user with given user object. This is with respect to username
     * @author Timmy Olsson, c12ton
     *
     * @param user user object to be updated
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
     * Checks if given username already exists in the database.
     * @author Timmy Olsson, c12ton
     *
     * @param userName user name for user.
     * @return true if it exists else false
     */
    private boolean userNameExist(String userName) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  UserName = ?)";

        Object[] args = new Object[]{userName};
        int[] types = new int[]{Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,Boolean.class);
    }

    /**
     * Checks if given email already  exists in the database.
     * @author Timmy Olsson, c12ton
     *
     * @param email email to be checked if it exist
     * @return  boolean if the email exist or not
     */
    private boolean emailExists(String email) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  email = ?)";

        Object[] args = {email};
        int[] types = {Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,Boolean.class);
    }

    /**
     * Return email for a user
     * @author c13elt, sanna
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
     * @author c13elt, sanna
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

	/**
     *  Used to collect user information, and return a hashmap.
     *  @author Timmy Olsson
     */
    private class UserWrapper implements org.springframework.jdbc.core.RowMapper {

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

