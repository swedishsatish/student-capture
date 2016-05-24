package studentcapture.course;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.user.User;
import studentcapture.user.UserDAO;

/**
 * Unit tests. 
 * 
 * @author tfy12hsm
 *
 */
public class CourseDAOTest extends StudentCaptureApplicationTests {

	@Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private JdbcTemplate jdbcMock;

    private CourseModel courseSetup;
	
	@Before
	public void setUp() {
		//Add one course
        String sql = "INSERT INTO course"
                +" (courseid, year, term, coursename, coursedescription, active)"
                +" VALUES (DEFAULT, ?, ?, ?, ?, ?)";

        courseSetup = new CourseModel();
        courseSetup.setYear(2016);
        courseSetup.setTerm("VT");
        courseSetup.setCourseName("Programvaruteknik");
        courseSetup.setCourseDescription("En kurs.");
        courseSetup.setActive(true);

        Object[] args = new Object[] {courseSetup.getYear(), courseSetup.getTerm(),
                            courseSetup.getCourseName(),courseSetup.getCourseDescription(),
                            courseSetup.getActive()};

        int[] types = new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,
                Types.VARCHAR,Types.BOOLEAN};

        try {
            jdbcMock.update(sql,args,types);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	@After
	public void tearDown() {
		String sql1 = "DELETE FROM Course;";
        String sql2 = "ALTER TABLE course ALTER COLUMN courseid RESTART WITH 1";
        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
	}

//	@Test
//	public void testAddCourse() {
//		CourseModel course = new CourseModel();
//		course.setYear(2017);
//        course.setTerm("HT");
//        course.setCourseName("Kurs2");
//        course.setCourseDescription("En kurs 2.");
//        course.setActive(false);
//		
//        CourseModel result = courseDAO.addCourse(course);
//        
//        course.setCourseId(2);
//        assertEquals(result,course);
//	}
//
//	@Test
//	public void testGetCourseWithoutID() {
//		CourseModel course = new CourseModel();
//		course.setYear(courseSetup.getYear());
//        course.setTerm(courseSetup.getTerm());
//        course.setCourseName(courseSetup.getCourseName());
//        course.setCourseDescription(courseSetup.getCourseDescription());
//        course.setActive(courseSetup.getActive());
//		
//        CourseModel result = courseDAO.getCourseWithoutID(course);
//        
//        assertTrue(result.getCourseId()!=null);
//        assertEquals(course.getYear(), result.getYear());
//        assertEquals(course.getTerm(), result.getTerm());
//        assertEquals(course.getCourseName(), result.getCourseName());
//	}
//
//	@Test
//	public void testGetCourseCourseModel() {
//		CourseModel course = new CourseModel();
//		course.setCourseId(1);
//		CourseModel result = courseDAO.getCourse(course);
//		
//		result.setCourseId(courseSetup.getCourseId());
//		assertEquals(result, courseSetup);
//	}

//	@Test
//	public void testGetCourseInteger() {
//		CourseModel result = courseDAO.getCourse(1);
//		result.setCourseId(courseSetup.getCourseId());
//		assertEquals(result, courseSetup);
//	}
//
	@Test
	public void testUpdateCourse() {
		courseSetup.setCourseId(1);
		courseSetup.setCourseDescription("nagat annat");
		courseSetup.setYear(1234);
		courseSetup.setTerm("SO");
		courseSetup.setCourseName("nagat");
		
		CourseModel result1 = courseDAO.updateCourse(courseSetup);
		CourseModel result2 = courseDAO.getCourse(courseSetup);
		
		//assertTrue(courseSetup.equals(result1));
		assertTrue(courseSetup.equals(result2));
	}

	@Test
	public void testRemoveCourseCourseModel() {
		CourseModel course = new CourseModel();
		course.setCourseId(1);
		CourseModel result1 = courseDAO.removeCourse(course);
		CourseModel result2 = courseDAO.getCourse(course);
		
		result1.setCourseId(courseSetup.getCourseId());
		assertEquals(result1, courseSetup);
		assertTrue(result2.getCourseId()==null);
	}

	@Test
	public void testRemoveCourseInteger() {
		CourseModel result1 = courseDAO.removeCourse(1);
		CourseModel result2 = courseDAO.getCourse(1);
		
		result1.setCourseId(courseSetup.getCourseId());
		assertEquals(result1, courseSetup);
		assertTrue(result2.getCourseId()==null);
	}
}
