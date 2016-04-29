package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
@Repository
public class User {


    private final String SQL_ADD_USR = "INSERT INTO users"
                                       + " (username, firstname, lastname, persnr, pswd)"
                                        + " VALUES (?, ?, ?, ?, ?);";

    private final String REM_USR_STATEMENT = "";

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new user to the User-table in the database.
     *
     * @param fName     First name of a user
     * @param lName     Last name of a user
     * @param pNr       Person-Number
     * @param pwd       Password
     * @param userName  unique user name
     * @param userID    unique identifier for a person
     * @return          true if success, else false.

     */
    public boolean addUser(String userName, String fName, String lName, String pNr, String pwd) {

        Object[] values = new Object[] {userName, fName,lName,pNr,pwd};
        Object[] types = new Object[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.CHAR,Types.VARCHAR};

        try {

            jdbcTemplate.update(SQL_ADD_USR, values);

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

    public List<Object> getUser(String casID) {

        //TODO

        return null;

    }



}

