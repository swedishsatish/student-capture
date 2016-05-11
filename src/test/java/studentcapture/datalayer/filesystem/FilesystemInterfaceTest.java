package studentcapture.datalayer.filesystem;

import javassist.bytecode.ByteArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.feedback.FeedbackModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
        FeedbackModel model = createFeedbackModel();

        createMockFile();
        FilesystemInterface.storeFeedbackText(model,testFile);
        File storedFile = new File(FilesystemInterface.generatePathFromModel(model)+FilesystemConstants.FEEDBACK_TEXT_FILENAME);
        assertTrue(storedFile.exists());
        //assertNotNull(FilesystemInterface.getFeedbackText(model),"Hej");

    }



    @Test
    public void shouldGetFeedbackText() throws Exception {
        FeedbackModel model = createFeedbackModel();

        String mockString = "First line in testfile";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(model,testFile);
        assertEquals(FilesystemInterface.getFeedbackText(model),"First line in testfile");
    }
    @Test
    public void shouldGetIncorrectFeedbackText() throws Exception {
        FeedbackModel model = createFeedbackModel();

        String mockString = "Hejsvejs";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(model,testFile);

        assertNotEquals(FilesystemInterface.getFeedbackText(model),"First line in testfile");
    }
    @Test
    public void shouldGetMultipleLineFeedbackText() throws Exception {
        FeedbackModel model = createFeedbackModel();

        String mockString = "First line in testfile\n" +
                "Second line in testfile\n"+
                "Third line in testfile";
        testFile = new MockMultipartFile("mockTestFile",mockString.getBytes());
        FilesystemInterface.storeFeedbackText(model,testFile);

        assertNotEquals(FilesystemInterface.getFeedbackText(model),mockString);
    }
    @Test
    public void shouldReturnNoFile() throws Exception {
        FeedbackModel model = createFeedbackModel();

        assertEquals(FilesystemInterface.getFeedbackText(model),"");
    }

    private FeedbackModel createFeedbackModel() {
        FeedbackModel model = new FeedbackModel();
        model.setCourseID(courseID);
        model.setAssignmentID(Integer.parseInt(assignmentID));
        model.setCourseCode(courseCode);
        model.setStudentID(15);
        return model;
    }

    private void createMockFile() {
        byte[] mockBytes = {1,2,4};
        testFile = new MockMultipartFile("mockTestFile", mockBytes);
    }
}
