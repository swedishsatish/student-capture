package studentcapture.datalayer.filesystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.submission.Submission;

import java.io.*;

import static org.junit.Assert.*;

public class FilesystemInterfaceTest {
    private String courseCode;
    private String courseID;
    private String assignmentID;
    private String userID;
    private MultipartFile testFile;

    @Before
    public void setUp() throws Exception {
        courseCode = "test";
        courseID = "5DV151";
        assignmentID = "1337";
        userID = "user";

    }

    /**
     * tearDown deletes the path created after each test.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        deleteFile(new File(StudentCaptureApplication.ROOT+"/moose/"+courseCode));
        deleteFile(new File(StudentCaptureApplication.ROOT+"/moose/"+courseID));
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    public static void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }

    @Test
	public void testGeneratePathWithoutStudent() {
		String path = FilesystemInterface.generatePath("5DV151", "1", "123");
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/");
	}
	
	@Test
	public void testGeneratePathWithStudent() {
		String path = FilesystemInterface.generatePath("5DV151", "1", "654","123");
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/654/");
	}

	@Test
	public void shouldStoreFileToNewPath(){
        createMockFile();
		FilesystemInterface.storeStudentVideo(courseCode,courseID,assignmentID,userID,testFile);

		File storedFile = new File(StudentCaptureApplication.ROOT+"/moose/"+courseCode+"/"+courseID+
                "/"+userID+"/"+assignmentID+"/submission.webm");
        assertTrue(storedFile.exists());
	}

    @Test
    public void shouldStoreFileToExistingPath(){
        byte[] mockBytes = {1,2,4};
        testFile = new MockMultipartFile("mockTestFile", mockBytes);
        FilesystemInterface.storeStudentVideo(courseCode,courseID,assignmentID,userID,testFile);
        testFile = new MockMultipartFile("mockTestFileExists", mockBytes);
        FilesystemInterface.storeStudentVideo(courseCode,courseID,assignmentID,userID,testFile);
        File storedFile = new File(StudentCaptureApplication.ROOT+"/moose/"+courseCode+"/"+courseID+
                "/"+userID+"/"+assignmentID+"/submission.webm");

        assertTrue(storedFile.exists());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException(){
        FilesystemInterface.storeStudentVideo("test","5DV151","1337","user",null);

    }

    @Test
    public void shouldFindFile() throws Exception {
        Submission submission = createSubmissionModel();

        createMockFile();
        FilesystemInterface.storeFeedbackText(submission,testFile);
        File storedFile = new File(FilesystemInterface.generatePathFromModel(submission)+FilesystemConstants.FEEDBACK_TEXT_FILENAME);
        assertTrue(storedFile.exists());
        //assertNotNull(FilesystemInterface.getFeedbackText(model),"Hej");

    }



    @Test
    public void shouldGetFeedbackText() throws Exception {
        Submission submission = createSubmissionModel();

        String mockString = "First line in testfile";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(submission,testFile);
        assertEquals(FilesystemInterface.getFeedbackText(submission),"First line in testfile");
    }
    @Test
    public void shouldGetIncorrectFeedbackText() throws Exception {
        Submission submission = createSubmissionModel();

        String mockString = "Hejsvejs";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(submission,testFile);

        assertNotEquals(FilesystemInterface.getFeedbackText(submission),"First line in testfile");
    }
    @Test
    public void shouldGetMultipleLineFeedbackText() throws Exception {
        Submission submission = createSubmissionModel();

        String mockString = "First line in testfile\n" +
                "Second line in testfile\n"+
                "Third line in testfile";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(submission,testFile);

        assertNotEquals(FilesystemInterface.getFeedbackText(submission),mockString);
    }
    @Test
    public void shouldReturnNoFile() throws Exception {
        Submission submission = createSubmissionModel();

        assertEquals(FilesystemInterface.getFeedbackText(submission),"");
    }

    @Test
    public void shouldCreateNewFile() throws IOException {
        String path;
        File file;

        FilesystemInterface.storeAssignmentText(courseID, assignmentID, "This is a test",
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);

        path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseID + "/" + assignmentID + "/" +
            FilesystemConstants.ASSIGNMENT_RECAP_FILENAME;
        file = new File(path);

        assertTrue(file.isFile());
    }

    @Test
    public void shouldOverwriteExistingFile() throws IOException {
        String path;
        File file;
        FileReader fileReader;
        char[] buf = new char[22];
        String content;

        FilesystemInterface.storeAssignmentText(courseID, assignmentID, "This should be overwritten",
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
        FilesystemInterface.storeAssignmentText(courseID, assignmentID, "This should be in file",
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);

        path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseID + "/" + assignmentID + "/" +
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME;
        file = new File(path);
        fileReader = new FileReader(file);
        fileReader.read(buf);
        fileReader.close();
        content = new String(buf);

        assertEquals("This should be in file", content);

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionAssignment(){
        FilesystemInterface.storeAssignmentVideo("test","1337",null);
    }
    @Test
    public void shouldStoreFileToNewPathAssignment(){
        createMockFile();
        FilesystemInterface.storeAssignmentVideo(courseID,assignmentID,testFile);

        File storedFile = new File(StudentCaptureApplication.ROOT + "/moose/"+
                "/" + courseID + "/" + assignmentID + "/assignment.webm");
        assertTrue(storedFile.exists());
    }

    private Submission createSubmissionModel() {
        Submission submission = new Submission();
        submission.setCourseID(courseID);
        submission.setAssignmentID(Integer.parseInt(assignmentID));
        submission.setCourseCode(courseCode);
        submission.setStudentID(15);
        return submission;
    }

    private void createMockFile() {
        byte[] mockBytes = {1,2,4};
        testFile = new MockMultipartFile("mockTestFile", mockBytes);
    }
}
