package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.AssignmentDAO.AssignmentWrapper;
import studentcapture.datalayer.database.SubmissionDAO.SubmissionWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public class UserDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private CourseDAO course;
    @Autowired
    private AssignmentDAO assignment;
    @Autowired
    private SubmissionDAO submissionDAO;

    /**
     * Add a new user to the User-table in the database.
     * @param user  instance that contains information of the user to be added.
     */
    public void addUser(User user) {

        String sql = "INSERT INTO users"
                + " (username, firstname, lastname, email, pswd)"
                + " VALUES (?, ?, ?, ?, ?)";

        Object[] args = new Object[] {user.getUserName(),user.getfName(),
                                      user.getlName(),user.getEmail(),
                                      user.getPswd()};

        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.VARCHAR,Types.VARCHAR};
        try {
            jdbcTemplate.update(sql, args,types);
        } catch (DataIntegrityViolationException e) {
		}
    }


    /**
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
     *
     * @param email
     * @return true if email exist else false
     */
    public boolean emailExist(String email) {
        String sql     = "SELECT EXISTS (SELECT 1 FROM users "
                        +"WHERE  Email = ?)";

        Object[] args = new Object[]{email};

        int[] types = new int[]{Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,
                                            Boolean.class);
    }

    /**
     * Remove a user from the User-table in the database.
     *
     * @param username     unique identifier for a person
     * @return          true if the remove succeed, else false.
     */
    public String getUserID(String username){
    	String sql = "SELECT userID from users WHERE username = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{username},String.class);
    }

    public boolean removeUser(String casID) {
        String sql = "";

        throw new UnsupportedOperationException();
    }


    /**
     * Updates a user in the User-table in the database. Use Null-value for
     * those parameters that shouldn't update.
     *
     * @param fName     First name of a user
     * @param lName     Last name of a user
     * @param pNr       Person-Number
     * @param pWord     Password
     * @param casID     unique identifier for a person
     * @return          true if the update succeed, else false.
     */
    public boolean updateUser(String fName, String lName, String pNr,
                              String pWord, String casID) {
		throw new UnsupportedOperationException();
    }

    /**
     * Returns a list with info of a user.
     *
     * @param userID     unique identifier for a person
     * @return          The list with info of a person.
     */
    public User getUserByID(String userID) {
        String sql = "SELECT  * FROM users WHERE userid = ?";


        Object[] arg = new Object[]{Integer.parseInt(userID)};
        User user = (User) jdbcTemplate.queryForObject(sql, arg,
                    new UserWrapper());

        return user;
    }


    /**
     * Check if a user exist by the name and password.
     * @return true if user exist, otherwise false.
     */
    public boolean userExist(String userName,String pswd) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  username = ? AND pswd = ?)";

        return jdbcTemplate.queryForObject(sql,
				new Object[] {userName,pswd},
				Boolean.class);
    }

	/**
     *  Used to collect user information, and return a hashmap.
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User(rs.getString("username"),rs.getString("firstname"),
                                 rs.getString("lastname"),rs.getString("email"),
                                 rs.getString("pswd"));
            return user;
        }
    }
}

