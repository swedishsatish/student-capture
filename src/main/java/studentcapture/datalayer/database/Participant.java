package studentcapture.datalayer.database;

import java.util.List;

/**
 * Created by c13gan on 2016-04-26.
 */
public class Participant {



    /**

     * Add a new participant to a course by connecting the tables "User" and "Course" in the database.

     *

     * @param CAS_ID        unique identifier for a person

     * @param CourseID      unique identifier, registration code

     * @param function      student, teacher, ....

     * @return              true if insertion worked, else false

     */

    private boolean addParticipant(String CAS_ID, String CourseID, String function){

        //TODO

        return true;

    }



    /**

     * Get the role for a participant in a selected course

     *

     * @param CAS_ID        unique identifier for a person

     * @param CourseID      unique identifier, registration code

     * @return              role of a person

     */

    private String getFunctionForParticipant(String CAS_ID, String CourseID){

        //TODO

        return "";

    }



    /**

     * Returns a list of all participants of a course including their function

     *

     * @param CourseID      unique identifier, registration code

     * @return              List of tuples: CAS_ID - function

     */

    private List<Object> getAllParticipantsFromCourse(String CourseID){

        //TODO

        return null;

    }



    /**

     * Returns a list of all courses a person is registered for, including their function

     *

     * @param CAS_ID        unique identifier for a person

     * @return              List of tuples: CourseID - function

     */

    private List<Object> getAllCoursesForParticipant(String CAS_ID){

        //TODO

        return null;

    }



    /**

     * Removes the connection between a person and a course from the database

     *

     * @param CAS_ID        unique identifier for a person

     * @param CourseID      unique identifier, registration code

     * @return              true if removal worked, else false

     */

    private boolean removeUser(String CAS_ID, String CourseID){

        //TODO

        return true;

    }

}
