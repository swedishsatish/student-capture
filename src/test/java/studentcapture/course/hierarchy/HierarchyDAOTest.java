package studentcapture.course.hierarchy;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;

public class HierarchyDAOTest extends StudentCaptureApplicationTests {

	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
    private HierarchyDAO hierarchyDAO;
    
    @Autowired
    private JdbcTemplate jdbcMock;
	
	@Before
	public void setUp() throws Exception {
		tearDown();
		jdbcMock.update("INSERT INTO users (username, firstname, lastname, "
				+ "email, pswd) VALUES ('testUser', 'testFName', 'testLName',"
				+ " 'testEmail@example.com', 'testPassword')");
		jdbcMock.update("INSERT INTO users (username, firstname, lastname, "
				+ "email, pswd) VALUES ('testUser2', 'testFName2', 'testLName2',"
				+ " 'testEmail2@example.com', 'testPassword2')");
	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
	    		+ "coursename, coursedescription, active) VALUES "
	    		+ "(DEFAULT, 2016, 'VT', 'Kurs1', 'En kurs.', true);");
	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
	    		+ "coursename, coursedescription, active) VALUES "
	    		+ "(DEFAULT, 2016, 'VT', 'Kurs2', 'En kurs.', true);");
	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
	    		+ "coursename, coursedescription, active) VALUES "
	    		+ "(DEFAULT, 2016, 'VT', 'Kurs3', 'En kurs.', false);");
	    jdbcMock.update("INSERT INTO course (courseid, year, term, "
	    		+ "coursename, coursedescription, active) VALUES "
	    		+ "(DEFAULT, 2016, 'VT', 'Kurs4', 'En kurs.', true);");
	    jdbcMock.update("INSERT INTO Participant VALUES (1,1, 'student');");
	    jdbcMock.update("INSERT INTO Participant VALUES (1,2, 'teacher');");
	    jdbcMock.update("INSERT INTO Participant VALUES (1,3, 'teacher');");
	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                "Published, GradeScale) VALUES (DEFAULT ,1,'Test1'," +
                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
                "0,100,'2016-05-01 01:00:00','NUMBER_SCALE');");
	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                "Published, GradeScale) VALUES (DEFAULT ,1,'Test2'," +
                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
                "0,100,null,'NUMBER_SCALE');");
	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                "Published, GradeScale) VALUES (DEFAULT ,2,'Test3'," +
                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
                "0,100,'2016-05-01 01:00:00','NUMBER_SCALE');");
	    jdbcMock.update("INSERT INTO Assignment (AssignmentID, " +
                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                "Published, GradeScale) VALUES (DEFAULT ,2,'Test4'," +
                "'2016-05-01 02:00:00', '2016-05-01 11:00:00'," +
                "0,100,null,'NUMBER_SCALE');");
	    jdbcMock.update("INSERT INTO Submission (assignmentId,studentId,"
	    		+ "studentpublishconsent,submissiondate) VALUES (1, 1, true,"
	    		+ "'2016-05-01 03:00:00');");
	    jdbcMock.update("INSERT INTO Submission (assignmentId,studentId,"
	    		+ "studentpublishconsent,submissiondate) VALUES (1, 2, true,"
	    		+ "'2016-05-01 03:00:00');");
	    jdbcMock.update("INSERT INTO Submission (assignmentId,studentId,"
	    		+ "studentpublishconsent,submissiondate) VALUES (3, 1, true,"
	    		+ "'2016-05-01 03:00:00');");
	    jdbcMock.update("INSERT INTO Submission (assignmentId,studentId,"
	    		+ "studentpublishconsent,submissiondate) VALUES (3, 2, true,"
	    		+ "'2016-05-01 03:00:00');");

	}

	@After
	public void tearDown() throws Exception {
		jdbcMock.update("DELETE FROM Submission;");
		jdbcMock.update("DELETE FROM Assignment;");
        jdbcMock.update("ALTER TABLE assignment ALTER COLUMN assignmentid RESTART WITH 1");
        jdbcMock.update("DELETE FROM Participant;");
        jdbcMock.update("DELETE FROM Users;");
        jdbcMock.update("ALTER TABLE users ALTER COLUMN userid RESTART WITH 1");
        jdbcMock.update("DELETE FROM Course;");
        jdbcMock.update("ALTER TABLE course ALTER COLUMN courseid RESTART WITH 1");
	}

	@Test
	public void testOnlyReturnForValidUser() {
		Optional<HierarchyModel> hierarchy1 = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		Optional<HierarchyModel> hierarchy2 = 
				hierarchyDAO.getCourseAssignmentHierarchy(2);
		Optional<HierarchyModel> hierarchy3 = 
				hierarchyDAO.getCourseAssignmentHierarchy(3);
		
		assertTrue(hierarchy1.isPresent());
		assertTrue(hierarchy2.isPresent());
		assertFalse(hierarchy3.isPresent());
	}
	
	@Test
	public void testHierarchyUserInfo() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertTrue(hierarchy.isPresent());
		assertEquals(hierarchy.get().getUserId(),1);
		assertEquals(hierarchy.get().getFirstName(),"testFName");
		assertEquals(hierarchy.get().getLastName(),"testLName");
	}

	@Test
	public void testTeacherHierarchy() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getTeacherCourses().size(),2);
		assertTrue(hierarchy.get().getTeacherCourses().get(2)!=null);
		assertTrue(hierarchy.get().getTeacherCourses().get(3)!=null);
	}
	
	@Test
	public void testStudentHierarchy() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getStudentCourses().size(),1);
		assertTrue(hierarchy.get().getStudentCourses().get(1)!=null);
	}
	
	@Test
	public void testTeacherAssignments() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getTeacherCourses().get(2).getAssignments().size(),2);
		assertEquals(hierarchy.get().getTeacherCourses().get(2).getAssignments().get(3).getAssignment().getTitle(),"Test3");
		assertEquals(hierarchy.get().getTeacherCourses().get(2).getAssignments().get(4).getAssignment().getTitle(),"Test4");
	}
	
	@Test
	public void testStudentAssignments() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getStudentCourses().get(1).getAssignments().size(),1);
		assertEquals(hierarchy.get().getStudentCourses().get(1).getAssignments().get(1).getAssignment().getTitle(),"Test1");
	}
	
	@Test
	public void testTeacherSubmissions() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getTeacherCourses().get(2).getAssignments().get(3).getSubmissions().size(),2);
		assertTrue(hierarchy.get().getTeacherCourses().get(2).getAssignments().get(3).getSubmissions().get(1)!=null);
		assertTrue(hierarchy.get().getTeacherCourses().get(2).getAssignments().get(3).getSubmissions().get(2)!=null);
	}
	
	@Test
	public void testStudentSubmissions() {
		Optional<HierarchyModel> hierarchy = 
				hierarchyDAO.getCourseAssignmentHierarchy(1);
		
		assertEquals(hierarchy.get().getStudentCourses().get(1).getAssignments().get(1).getSubmissions().size(),1);
		assertTrue(hierarchy.get().getStudentCourses().get(1).getAssignments().get(1).getSubmissions().get(1)!=null);
	}
}
