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

public class CourseDAOTest extends StudentCaptureApplicationTests {

	@Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private JdbcTemplate jdbcMock;

    private CourseModel courseSetup;
	
//	@Before
//	public void setUp() throws Exception {
//		//Add one course
//        String sql = "INSERT INTO course"
//                +" (year, term, coursename, coursedescription, active)"
//                +" VALUES (?, ?, ?, ?, ?)";
//
//        courseSetup = new CourseModel();
//        courseSetup.setYear(2016);
//        courseSetup.setTerm("VT");
//        courseSetup.setCourseName("Programvaruteknik");
//        courseSetup.setCourseDescription("En kurs.");
//        courseSetup.setActive(true);
//
//        Object[] args = new Object[] {courseSetup.getYear(), courseSetup.getTerm(),
//                            courseSetup.getCourseName(),courseSetup.getCourseDescription(),
//                            courseSetup.getActive()};
//
//        int[] types = new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,
//                Types.VARCHAR,Types.BOOLEAN};
//
//        try {
//            jdbcMock.update(sql,args,types);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		String sql1 = "DELETE FROM Course;";
//        String sql2 = "ALTER TABLE course ALTER COLUMN courseid RESTART WITH 1";
//        jdbcMock.update(sql1);
//        jdbcMock.update(sql2);
//	}
//
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
//        assertEquals(result.getCourseId(),new Integer(2));
//	}
//
//	@Test
//	public void testGetCourseWithoutID() {
//	}
//
//	@Test
//	public void testGetCourseCourseModel() {
//	}
//
//	@Test
//	public void testGetCourseInteger() {
//	}
//
//	@Test
//	public void testUpdateCourse() {
//	}
//
//	@Test
//	public void testRemoveCourseCourseModel() {
//	}
//
//	@Test
//	public void testRemoveCourseInteger() {
//	}
//
//	@Test
//	public void testGetCourseCodeFromId() {
//	}

}
