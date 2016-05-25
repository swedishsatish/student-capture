package studentcapture.datalayer.filesystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.submission.Submission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class FilesystemInterfaceTest {
    private String courseCode;
    private String courseID;
    private String assignmentID;
    private MultipartFile testFile;

    @Before
    public void setUp() throws Exception {
        courseCode = "test";
        courseID = "5DV151";
        assignmentID = "1337";

    }

    /**
     * tearDown deletes the path created after each test.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        deleteFile(new File(StudentCaptureApplication.ROOT+"/moose/"+courseID));
        deleteFile(new File(StudentCaptureApplication.ROOT+"/moose/"+151));
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
    public void shouldStoreFileToExistingPath(){
        byte[] mockBytes = {1,2,4};
        testFile = new MockMultipartFile("mockTestFile", mockBytes);
        Submission submission = createSubmissionModel();
        FilesystemInterface.storeStudentVideo(submission, testFile);
        testFile = new MockMultipartFile("mockTestFileExists", mockBytes);
        FilesystemInterface.storeStudentVideo(submission, testFile);
        File storedFile = new File(FilesystemInterface.generatePath(submission) + FilesystemConstants.SUBMISSION_VIDEO_FILENAME);

        assertTrue(storedFile.exists());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException(){
        Submission submission = createSubmissionModel();
        FilesystemInterface.storeStudentVideo(submission, null);

    }

    @Test
    public void shouldFindFile() throws Exception {
        Submission submission = createSubmissionModel();

        createMockFile();
        FilesystemInterface.storeFeedbackText(submission, testFile);
        File storedFile = new File(FilesystemInterface.generatePath(submission)+FilesystemConstants.FEEDBACK_TEXT_FILENAME);
        assertTrue(storedFile.exists());

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

        FilesystemInterface.storeAssignmentText(151, assignmentID, "This is a test",
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);

        path = FilesystemConstants.FILESYSTEM_PATH + "/" + 151 + "/" + assignmentID + "/" +
            FilesystemConstants.ASSIGNMENT_RECAP_FILENAME;
        file = new File(path);

        assertTrue(file.isFile());
    }

    @Test
    public void shouldGetFileContentsAsString() throws IOException {
        String fileContents;

        FilesystemInterface.storeAssignmentText(151, assignmentID, "The file contains this",
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
        fileContents = FilesystemInterface.getAssignmentText(151, assignmentID,
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);

        assertEquals("The file contains this", fileContents);
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowWhenAssignmentTextDontExist() throws IOException {
        FilesystemInterface.getAssignmentText(151, assignmentID, FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
    }

    @Test
    public void shouldOverwriteExistingFile() throws IOException {
        String path;
        File file;
        FileReader fileReader;
        char[] buf = new char[22];
        String content;

        FilesystemInterface.storeAssignmentText(151, assignmentID, "This should be overwritten",
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
        FilesystemInterface.storeAssignmentText(151, assignmentID, "This should be in file",
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);

        path = FilesystemConstants.FILESYSTEM_PATH + "/" + 151 + "/" + assignmentID + "/" +
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME;
        file = new File(path);
        fileReader = new FileReader(file);
        fileReader.read(buf);
        fileReader.close();
        content = new String(buf);

        assertEquals("This should be in file", content);

    }

    @Test
    public void shouldDeleteAssignmentDir() throws Exception {
        String path = FilesystemConstants.FILESYSTEM_PATH + "/" + 151 + "/" + assignmentID + "/";
        File file = new File(path);

        FilesystemInterface.storeAssignmentText(151, assignmentID, "This doesn't matter",
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
        FilesystemInterface.storeAssignmentText(151, assignmentID, "This doesn't matter",
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
        FilesystemInterface.deleteAssignmentFiles(151, Integer.parseInt(assignmentID));

        assertTrue(!file.exists());

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
