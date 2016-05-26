package studentcapture.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by c13gan on 2016-04-26.
 */
@Repository
public class CourseDAO {
    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Attempts to add a course to the database.
     *
     * @param course	added course
     * @return			added course. Includes errorcode if problem has occured
     */
    public CourseModel addCourse(CourseModel course) {
        String addCourseStatement =
                "INSERT INTO Course VALUES (DEFAULT,?,?,?,?,?)";

        try {
            int rowsAffected = jdbcTemplate.update(addCourseStatement,
                    course.getYear(),
                    course.getTerm(),
                    course.getCourseName(),
                    course.getCourseDescription(), 
                    course.getActive());
            if (rowsAffected == 1) {
            	return getCourseWithoutID(course);
            } else {
            	CourseModel errorCourse = new CourseModel();
            	errorCourse.setErrorCode(HttpStatus.CONFLICT.value());
            	return errorCourse;
            }
        } catch (IncorrectResultSizeDataAccessException e){
        	CourseModel errorCourse = new CourseModel();
        	errorCourse.setErrorCode(HttpStatus.NOT_FOUND.value());
        	return errorCourse;
        } catch (DataAccessException e1){
            CourseModel errorCourse = new CourseModel();
        	errorCourse.setErrorCode(HttpStatus.CONFLICT.value());
        	return errorCourse;
            
        }
    }

    /**
     * Returns latest course with similar data. Used to find course without
     * knowing it's id.
     * 
     * @param course	course to compare to
     * @return			course found
     */
    public CourseModel getCourseWithoutID(CourseModel course){
    	try {
    		String sql = "SELECT * from course WHERE Year=? "
        			+ "AND Term=? AND CourseName=?";
    		List<Map<String,Object>> sqlResponse = jdbcTemplate.queryForList(
    				sql, course.getYear(),
					course.getTerm(),
					course.getCourseName());
        	return new CourseModel(sqlResponse.get(sqlResponse.size() - 1));
    	} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
    }
    
    /**
     * Attempts to retrieve all data regarding a course from the database.
     *
     * @param course	course to find
     * @return			found course
     */
	public CourseModel getCourse(CourseModel course) {
		try {
            String getCourseStatement =
                    "SELECT * FROM Course WHERE CourseId=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
        			getCourseStatement, course.getCourseId());
            return new CourseModel(map);		
		} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
	}
	
	/**
     * Attempts to retrieve all data regarding a course from the database.
     *
     * @param courseId	target courses database identification
     * @return			sought after course
     */
	public CourseModel getCourse(Integer courseId) {
		try {
            String getCourseStatement =
                    "SELECT * FROM Course WHERE CourseId=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
        			getCourseStatement, courseId);
            return new CourseModel(map);		
		} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
	}

	/**
	 * 
	 * 
	 * @param course
	 * @return
	 */
	public CourseModel updateCourse(CourseModel course) {
		String changeDescriptionOnCourseStatement = "UPDATE Course SET "
				+ "Year=?,Term=?,CourseName=?,CourseDescription=?,Active=? "
				+ "WHERE CourseId=?";
		try {
            int rowsAffected = jdbcTemplate.update(
            		changeDescriptionOnCourseStatement, 
            		course.getYear(),
            		course.getTerm(),
            		course.getCourseName(),
            		course.getCourseDescription(),
            		course.getActive(),
            		course.getCourseId());
            if (rowsAffected == 1) {
            	return course;
            } else {
            	return new CourseModel();
            }
        } catch (IncorrectResultSizeDataAccessException e){
            return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
	}
	
    /**
     * Attempts to remove a course from the database.
     *
     * @param course	courses database identification
     * @return			true if successful, else false
     */
    public CourseModel removeCourse(CourseModel course) {
        String removeCourseStatement = "DELETE FROM Course WHERE CourseID=?";

        try {
        	course = getCourse(course);
        	
            int rowsAffected = jdbcTemplate.update(removeCourseStatement,
                    course.getCourseId());
            
            if(rowsAffected == 1) {
            	return course;
            } 
            return new CourseModel();
        } catch (IncorrectResultSizeDataAccessException e){
           	return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
    }
    
    public CourseModel removeCourse(Integer courseId) {
        String removeCourseStatement = "DELETE FROM Course WHERE CourseID=?";

        try {
        	CourseModel course = getCourse(courseId);
            int rowsAffected = jdbcTemplate.update(removeCourseStatement,
                    courseId);         
            if(rowsAffected == 1) {
            	return course;
            }
        } catch (DataAccessException e1){
        	CourseModel errorCourse = new CourseModel();
        	errorCourse.setErrorCode(HttpStatus.CONFLICT.value());
        	return errorCourse;
        }

        CourseModel errorCourse = new CourseModel();
    	errorCourse.setErrorCode(HttpStatus.NOT_FOUND.value());
    	return errorCourse;
    }

//    /**
//     * Fetches the code for a course.
//     * Useful when constructing a file path.
//     * @param courseId Unique identifier for the course
//     * @return course code
//     */
//    public String getCourseCodeFromId(CourseModel course){
//        String query = "SELECT coursecode FROM Course WHERE courseid = ?;";
//        String courseCode;
//
//        try {
//            courseCode = databaseConnection.queryForObject(query, new Object[]{course.getCourseId()}, String.class);
//            	
//            if (courseCode == null) {
//                courseCode = "Missing value";
//            } else {
//                courseCode = courseCode.trim();
//            }
//        } catch (IncorrectResultSizeDataAccessException up) {
//            throw up;
//        } catch (DataAccessException down) {
//            throw down;
//        }
//
//        return courseCode;
//    }
}
