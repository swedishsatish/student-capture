package studentcapture.datalayer.filesystem;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import java.io.File;

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
		byte[] mockBytes = {1,2,4};
        testFile = new MockMultipartFile("mockTestFile", mockBytes);
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

}
