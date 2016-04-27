package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c13gan on 2016-04-26.
 */
public class Participant {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;


    /**

     * Add a new participant to a course by connecting the tables "User" and "Course" in the database.

     *

     * @param CAS_ID        unique identifier for a person

     * @param CourseID      unique identifier, registration code

     * @param function      student, teacher, ....

     * @return              true if insertion worked, else false

     */
    
    private static final String addParticipantStatement = "INSERT INTO Participant VALUES (?,?,?)";
    private boolean addParticipant(int studentId, int courseId, String function) {
        boolean result;
        try {
            int rowsAffected = jdbcTemplate.update(addParticipantStatement, 
            		new Object[] {studentId, courseId});
            if(rowsAffected == 1) {
            	result = true;
            } else {
            	result = false;
            }
        }catch (IncorrectResultSizeDataAccessException e){
            result = false;
        }catch (DataAccessException e1){
            result = false;
        }
        
        return result;
    }



    /**

     * Get the role for a participant in a selected course

     *

     * @param CAS_ID        unique identifier for a person

     * @param CourseID      unique identifier, registration code

     * @return              role of a person

     */

    private String getFunctionForParticipant(String CAS_ID, String CourseID){

        //TODO "SELECT Position FROM Participant WHERE (UserId=? AND CourseId=?)"

        return "";

    }



    /**

     * Returns a list of all participants of a course including their function

     *

     * @param CourseID      unique identifier, registration code

     * @return              List of tuples: CAS_ID - function

     */

    private List<Object> getAllParticipantsFromCourse(String CourseID){

        //TODO "SELECT * FROM Participant WHERE (CourseId=?)"

        return null;

    }



    /**

     * Returns a list of all courses a person is registered for, including their function

     *

     * @param CAS_ID        unique identifier for a person

     * @return              List of tuples: CourseID - function

     */

    private List<Object> getAllCoursesForParticipant(String CAS_ID){

        //TODO "SELECT CourseId,Position FROM Participant WHERE (UserId=?)"

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

        //TODO "DELETE FROM Participant WHERE (UserId=? AND CourseId=?)"

        return true;

    }

}
