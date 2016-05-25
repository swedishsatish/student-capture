package studentcapture.course.hierarchy;

import studentcapture.config.StudentCaptureApplicationTests;

public class HierarchyDAOTest extends StudentCaptureApplicationTests {

//	@Autowired
//    private WebApplicationContext webApplicationContext;
//	
//	@Autowired
//    private CourseDAO courseDAO;
//    @Autowired
//    private AssignmentDAO assignmentDAO;
//    @Autowired
//    private SubmissionDAO submissionDAO;
//    @Autowired
//    private UserDAO userDAO;
//    
//    @Autowired
//    private JdbcTemplate jdbcMock;
//
//    private User userSetup1;
//    private CourseModel courseSetup1;
//    private CourseModel courseSetup2;
//    private CourseModel courseSetup3;
//    private Participant participantSetup1;
//    private Participant participantSetup2;
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
