package studentcapture.course.hierarchy;

import static org.junit.Assert.*;

import java.sql.Types;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.assignment.AssignmentDAO;
import studentcapture.assignment.AssignmentModel;

import studentcapture.config.StudentCaptureApplicationTests;

public class HierarchyDAOTest extends StudentCaptureApplicationTests {

//	@Autowired
//    private WebApplicationContext webApplicationContext;
//	
//	@Autowired
//    private HierarchyDAO hierarchyDAO;
//    
//    @Autowired
//    private JdbcTemplate jdbcMock;
//	
//	@Before
//	public void setUp() throws Exception {
//		jdbcMock.update("INSERT INTO users (username, firstname, lastname, "
//				+ "email, pswd) VALUES ('testUser', 'testFName', 'testLName',"
//				+ " 'testEmail@example.com', 'testPassword')");
//	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
//	    		+ "coursename, coursedescription, active) VALUES "
//	    		+ "(DEFAULT, 2016, 'VT', 'Kurs1', 'En kurs.', true);");
//	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
//	    		+ "coursename, coursedescription, active) VALUES "
//	    		+ "(DEFAULT, 2016, 'VT', 'Kurs2', 'En kurs.', true);");
//	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
//	    		+ "coursename, coursedescription, active) VALUES "
//	    		+ "(DEFAULT, 2016, 'VT', 'Kurs3', 'En kurs.', false);");
//	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
//	    		+ "coursename, coursedescription, active) VALUES "
//	    		+ "(DEFAULT, 2016, 'VT', 'Kurs4', 'En kurs.', true);");
//	    jdbcMock.update("INSERT INTO Participant VALUES (1,1, 'student');");
//	    jdbcMock.update("INSERT INTO Participant VALUES (1,2, 'teacher');");
//	    jdbcMock.update("INSERT INTO Participant VALUES (1,3, 'teacher');");
//	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
//                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
//                "Published, GradeScale) VALUES (DEFAULT ,1,'Test1'," +
//                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
//                "0,100,'2016-05-01 01:00:00','NUMBER_SCALE');");
//	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
//                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
//                "Published, GradeScale) VALUES (DEFAULT ,1,'Test2'," +
//                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
//                "0,100,null,'NUMBER_SCALE');");
//	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
//                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
//                "Published, GradeScale) VALUES (DEFAULT ,2,'Test3'," +
//                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
//                "0,100,'2016-05-01 01:00:00','NUMBER_SCALE');");
//	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
//                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
//                "Published, GradeScale) VALUES (DEFAULT ,2,'Test4'," +
//                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
//                "0,100,null,'NUMBER_SCALE');");
//	    jdbcMock.update("INSERT INTO Submission VALUES (1, 1, null,"
//	    		+ " null,'2016-05-01 03:00:00', null, null, null, null, "
//	    		+ "null);");
//	    jdbcMock.update("INSERT INTO Submission VALUES (1, 2, null,"
//	    		+ " null,'2016-05-01 03:00:00', null, null, null, null, "
//	    		+ "null);");
//	    jdbcMock.update("INSERT INTO Submission VALUES (3, 1, null,"
//	    		+ " null,'2016-05-01 03:00:00', null, null, null, null, "
//	    		+ "null);");
//	    jdbcMock.update("INSERT INTO Submission VALUES (3, 2, null,"
//	    		+ " null,'2016-05-01 03:00:00', null, null, null, null, "
//	    		+ "null);");
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		jdbcMock.update("DELETE FROM Submission;");
//		jdbcMock.update("DELETE FROM Assignment;");
//        jdbcMock.update("ALTER TABLE assignment ALTER COLUMN assignmentid RESTART WITH 1");
//        jdbcMock.update("DELETE FROM Participant;");
//        jdbcMock.update("DELETE FROM Users;");
//        jdbcMock.update("ALTER TABLE users ALTER COLUMN userid RESTART WITH 1");
//        jdbcMock.update("DELETE FROM Course;");
//        jdbcMock.update("ALTER TABLE course ALTER COLUMN courseid RESTART WITH 1");
//	}
//
//	@Test
//	public void test() {
//		Optional<HierarchyModel> hierarchy = hierarchyDAO.getCourseAssignmentHierarchy(1);
//		
//		assertTrue(hierarchy!=null);
//	}

    
    
//    private User userSetup1;
//    private CourseModel courseSetup1;
//    private CourseModel courseSetup2;
//    private CourseModel courseSetup3;
//    private AssignmentModel assignmentSetup1;
//    private AssignmentModel assignmentSetup2;
//    private AssignmentModel assignmentSetup3;
//    private AssignmentModel assignmentSetup4;
//    private Submission submissionSetup1;
//    private Submission submissionSetup2;
//    private Submission submissionSetup3;
//	
//	@Before
//	public void setUp() throws Exception {
//		//Add one user
//        String sql = "INSERT INTO users"
//                +" (username, firstname, lastname, email, pswd)"
//                +" VALUES (?, ?, ?, ?, ?)";
//
//        userSetup1 = new User("testUser","testFName","testLName",
//                             "testEmail@example.com","testPassword123");
//
//        Object[] args = new Object[] {userSetup1.getUserName(), userSetup1.getFirstName(),
//                            userSetup1.getLastName(),userSetup1.getEmail(),
//                            userSetup1.getPswd()};
//
//        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
//                Types.VARCHAR,Types.VARCHAR};
//
//        try {
//            jdbcMock.update(sql,args,types);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//      //Add three courses
//      sql = "INSERT INTO course"
//              +" (courseid, year, term, coursename, coursedescription, active)"
//              +" VALUES (DEFAULT, ?, ?, ?, ?, ?)";
//
//      courseSetup1 = new CourseModel();
//      courseSetup1.setYear(2016);
//      courseSetup1.setTerm("VT");
//      courseSetup1.setCourseName("Kurs1");
//      courseSetup1.setCourseDescription("En kurs.");
//      courseSetup1.setActive(true);
//
//      args = new Object[] {courseSetup1.getYear(), courseSetup1.getTerm(),
//                          courseSetup1.getCourseName(),courseSetup1.getCourseDescription(),
//                          courseSetup1.getActive()};
//
//      types = new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,
//              Types.VARCHAR,Types.BOOLEAN};
//
//      try {
//          jdbcMock.update(sql,args,types);
//      } catch(Exception e) {
//          e.printStackTrace();
//      }
//      
//
//      courseSetup2 = new CourseModel();
//      courseSetup2.setYear(2016);
//      courseSetup2.setTerm("VT");
//      courseSetup2.setCourseName("Kurs2");
//      courseSetup2.setCourseDescription("En kurs.");
//      courseSetup2.setActive(true);
//
//      args = new Object[] {courseSetup2.getYear(), courseSetup2.getTerm(),
//                          courseSetup2.getCourseName(),courseSetup2.getCourseDescription(),
//                          courseSetup2.getActive()};
//      try {
//          jdbcMock.update(sql,args,types);
//      } catch(Exception e) {
//          e.printStackTrace();
//      }
//      
//      
//      courseSetup3 = new CourseModel();
//      courseSetup3.setYear(2016);
//      courseSetup3.setTerm("VT");
//      courseSetup3.setCourseName("Kurs3");
//      courseSetup3.setCourseDescription("En kurs.");
//      courseSetup3.setActive(true);
//
//      args = new Object[] {courseSetup3.getYear(), courseSetup3.getTerm(),
//                          courseSetup3.getCourseName(),courseSetup3.getCourseDescription(),
//                          courseSetup3.getActive()};
//      try {
//          jdbcMock.update(sql,args,types);
//      } catch(Exception e) {
//          e.printStackTrace();
//      }
//      
//      // Adds participant to two courses.
//      sql = "INSERT INTO Participant VALUES (1,1, 'student');";
//      jdbcMock.update(sql);
//      sql = "INSERT INTO Participant VALUES (1,2, 'teacher');";
//      jdbcMock.update(sql);
//      
//      // Add assignments to courses.
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}

}
