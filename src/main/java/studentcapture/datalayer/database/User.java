package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.Assignment.AssignmentWrapper;
import studentcapture.datalayer.database.Course.CourseWrapper;
import studentcapture.datalayer.database.Submission.SubmissionWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Repository
public class User {


    private final String SQL_ADD_USR = "INSERT INTO users"
                                       + " (username, firstname, lastname, persnr, pswd)"
                                        + " VALUES (?, ?, ?, ?, ?)";

    private final String SQL_GET_USR_BY_ID = "SELECT  * FROM users WHERE userid = ?";
    private final String SQL_USR_EXIST     = "SELECT EXISTS (SELECT 1 FROM users "
                                           + "WHERE  username = ? AND pswd = ?)";

    private final String SQL_GET_PSWDS = "";
    private final String SQL_RM_USR   = "";

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new user to the User-table in the database.
     *
     * @param userName  unique identifier for a person
     * @param fName     First name of a user
     * @param lName     Last name of a user
     * @param pNr       Person-Number
     * @param pwd       Password
     * @return          true if success, else false.
     */
    public boolean addUser(String userName, String fName, String lName, String pNr, String pwd) {

        Object[] args = new Object[] {userName, fName,lName,pNr,pwd};
        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.CHAR,Types.VARCHAR};

        try {
            jdbcTemplate.update(SQL_ADD_USR, args,types);

        } catch (DataIntegrityViolationException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


    /**
     * Remove a user from the User-table in the database.
     *
     * @param casID     unique identifier for a person
     * @return          true if the remove succeed, else false.
     */


    public String getUserID(String username){
    	String sql = "SELECT userID from users WHERE username = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{username},String.class);
    }

    public boolean removeUser(String casID) {
        //TODO
        return false;

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
        //TODO
        return false;

    }


    /**
     * Returns a list with info of a user.
     *
     * @param casID     unique identifier for a person
     * @return          The list with info of a person.
     */
    public HashMap<String,String> getUserByID(String userID) {

        String sql = "SELECT * FROM users WHERE userid = ?";

        Object[] arg = new Object[]{Integer.parseInt(userID)};
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcTemplate.queryForObject(SQL_GET_USR_BY_ID,
                        arg, new UserWrapper());

        return info;
    }


    /**
     * Check if a user exist by the name and password.
     * @return true if user exist, otherwise false.
     */
    public boolean userExist(String userName,String pswd) {

        boolean exist = jdbcTemplate.queryForObject(SQL_USR_EXIST,
                                                    new Object[] {userName,pswd},
                                                     Boolean.class);
        return exist;
    }

    public Optional<CourseAssignmentHierarchy> getCourseAssignmentHierarchy() {
    	return null;
    }

    /**
     *  Used to collect user information, and return a hashmap.
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap();
            info.put("userid",rs.getString("userid"));
            info.put("username",rs.getString("username"));
            info.put("lastname",rs.getString("lastname"));
            info.put("persnr",rs.getString("persnr"));
            info.put("pswd",rs.getString("pswd"));
            return info;
        }
    }

    public class CourseAssignmentHierarchy {
    	public UserWrapper user;
    	public List<CoursePackage> courses;
    }

    public class CoursePackage {
    	public CourseWrapper course;
    	public List<AssignmentPackage> assignments;
    }

    public class AssignmentPackage {
    	public AssignmentWrapper assignment = null;
    	public SubmissionWrapper submission = null;
    }
}

