package studentcapture.datalayer.filesystem;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentModel;
import studentcapture.submission.Submission;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Interface to mangae the file system moosefs.
 *
 * Folders are structured as followed:
 *
 *  courseCode
 *   courseID
 *     assignmentID
 *      //assigment.avi
 *     studentID
 *        ansID
 *          //ans.avi
 */
public class FilesystemInterface {

	/**
	 * Generate path to a assignment
	 * @param assignment the assignment to generate path to
	 * @return the generated path
     */
	public static String generatePath(AssignmentModel assignment) {
		return FilesystemConstants.FILESYSTEM_PATH + "/" + assignment.getCourseID()
													+ "/" + assignment.getAssignmentID()  + "/";
	}

	/**
	 * Generate path to a submission
	 * @param submission the submission to generate path to
	 * @return the generated path
     */
	public static String generatePath(Submission submission) {
		System.out.println(submission.getCourseID() + " " + submission.getAssignmentID() + " " + submission.getStudentID() );
		return FilesystemConstants.FILESYSTEM_PATH + "/" + submission.getCourseID()
													+ "/" + submission.getAssignmentID()
	   												+ "/" + submission.getStudentID() + "/";
	}

	/**
	 * Gets the specified video on the file server.
	 * @param path 	The path to the file on the file server.
	 * @return The video as an InputStream contained in a HTTP ResponseEntity.
     */
	public static ResponseEntity<InputStreamResource> getVideo(String path) {
        if(path == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<InputStreamResource> responseEntity;
        File video = new File(path);

		try {
			byte[] out = FileCopyUtils.copyToByteArray(video);
			InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(out));

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "inline; filename=AssignmentVideo");

			responseEntity = new ResponseEntity<>(isr, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

	/**
	 * Store the students video for an assignment at a course.
	 * If student folder doesn't exist a folder will be created.
	 *
     * @param submission The submission which the video will be linked to.
     * @param source the video.
	 * @return true if video was stored successfully
	 */
	public static boolean storeStudentVideo(Submission submission, MultipartFile source) {

		String path = FilesystemInterface.generatePath(submission);
		try {
			storeFile(source ,path,FilesystemConstants.SUBMISSION_VIDEO_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Store the assignment video for an assignment at a course.
	 *
	 * @param courseID course id from the database.
	 * @param assignmentID from database.
	 * @return true if video was stored successfully, false otherwise.
	 */
	public static boolean storeAssignmentVideo(Integer courseID, String assignmentID, MultipartFile source) {

		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseID + "/" + assignmentID + "/";

		try {
			storeFile(source, path, FilesystemConstants.ASSIGNMENT_VIDEO_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	/**
	 * Store the teacher's feedback video to a student submission
	 * @param submission Submission object
	 * @param source Feedback video file
	 * @return true if video was stored successfully
	 */
	public static boolean storeFeedbackVideo(Submission submission,
									  MultipartFile source) {
		String path = FilesystemInterface.generatePath(submission);

		try {
			storeFile(source,path, FilesystemConstants.FEEDBACK_VIDEO_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Store the teacher's feedback text to a student submission
	 * @param submission Submission object
	 * @param source Feedback text file
     * @return true if text was stored successfully
     */
	public static boolean storeFeedbackText(Submission submission,
									 MultipartFile source) {
		String path = FilesystemInterface.generatePath(submission);

		try {
			storeFile(source,path,FilesystemConstants.FEEDBACK_TEXT_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Store the assignment recap
	 * @param assignment the assignment to store the recap for
	 * @return true if the recap could be saved otherwise false
     */
	public static boolean storeAssignmentRecap(AssignmentModel assignment) {
		String path = generatePath(assignment) + FilesystemConstants.ASSIGNMENT_RECAP_FILENAME;
		return printTextToFile(assignment.getRecap(), path);
	}

	/**
	 * Store the assignment description
	 * @param assignment the assignment to store the description for
	 * @return true if the description could be saved otherwise false
	 */
	public static boolean storeAssignmentDescription(AssignmentModel assignment) {
		String path = generatePath(assignment) + FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME;
		return printTextToFile(assignment.getDescription(), path);
	}

	public static String getAssignmentRecap(AssignmentModel assignment) {
		String path = generatePath(assignment) + FilesystemConstants.ASSIGNMENT_RECAP_FILENAME;
		return readContentFromFile(path);
	}

	public static String getAssignmentDescription(AssignmentModel assignment) {
		String path = generatePath(assignment) + FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME;
		return readContentFromFile(path);
	}


	public static void deleteAssignmentFiles(AssignmentModel assignment)
			throws IOException {
		String path = generatePath(assignment);
		FileUtils.deleteDirectory(new File(path));
	}

    /**
     * Reads a feedback text file from a teacher from the moose hard drive and returns it.
     * @param submission the submission model containing params to generate the path to the file.
     * @return the teacher's written feedback as a string.
     */
	public static String getFeedbackText(Submission submission) {
		String path = generatePath(submission)+FilesystemConstants.FEEDBACK_TEXT_FILENAME;
		return readContentFromFile(path);
	}

	/**
	 * Stores a file at given path, if path doesn't exist it will be
	 * created.
	 *
	 * @throws IOException in case that it can't create a folder at given path
	 * @param source the video file to be stored
	 * @param des destination for the video file
	 */
	private static void storeFile(MultipartFile source, String des, String filename) throws IOException {
		File dir = new File(des);
		dir.mkdirs();
		File file = new File(des +"/"+ filename);
		source.transferTo(file);
	}

	/**
	 * Prints some text to a existing or new file. The content in the file is overwritten
	 * @param text the text to print
	 * @param path the path to the file
     * @return true is successful
     */
	public static boolean printTextToFile(String text, String path) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try {
			PrintWriter out = new PrintWriter(file);
			out.print(text);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}

	}

	/**
	 * Read the content of a file
	 * @param path the path to the file
     * @return the content of the file or empty string on error
     */
	public static String readContentFromFile(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			return "";
		}

	}
}
