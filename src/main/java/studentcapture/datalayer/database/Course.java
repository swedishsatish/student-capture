package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by c13gan on 2016-04-26.
 */
public class Course {


    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    /**

     * Attempts to add a course to the database.

     *

     * @param courseCode	the courses 6 character identification (ex. 5DV151)

     * @param year		the year the course takes place

     * @param term		the term the course takes palce (ex. VT)

     * @return			true if successful, else false

     */

    public boolean addCourse(String courseCode, int year, String term) {

        return false; //TODO

    }



    /**

     * Attempts to retrieve all data regarding a course from the database.

     *

     * @param courseID	target courses database identification

     * @return			list of course data in order:<ul>

     * 					<li>the courses database identification</li>

     * 					<li>the courses 6 character identification</li>

     * 					<li>the year the course takes place</li>

     * 					<li>the term the course takes place</li>

     * 					<li>the courses name</li></ul>

     * 					If unsuccessful, an empty list is returned.

     */

    public List<Object> getCourse(int courseID) {

        return null; //TODO

    }



    /**

     * Attempts to remove a course from the database.

     *

     * @param courseID	courses database identification

     * @return			true if successful, else false

     */

    public boolean removeCourse(int courseID) {

        return false; //TODO

    }


}
