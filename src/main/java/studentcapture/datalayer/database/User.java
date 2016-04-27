package studentcapture.datalayer.database;

import java.util.List;

public class User {



    /**

     * Add a new user to the User-table in the database.

     *

     * @param fName     First name of a user

     * @param lName     Last name of a user

     * @param pNr       Person-Number

     * @param pWord     Password

     * @param casID     unique identifier for a person

     * @return          true if success, else false.

     */

    public boolean addUser(String fName, String lName, String pNr,

                           String pWord, String casID) {

        //TODO

        return false;

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

