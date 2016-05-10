package studentcapture.datalayer.filesystem;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.feedback.FeedbackModel;

import java.io.*;


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
	 * Generates a string representing a path to an assignments directory on
	 * the filesystem.
	 *
	 * @param courseCode	courses 6 character identifier
	 * @param courseId		courses unique database id
	 * @param assignmentId	assignments unique database id
	 * @return				path to directory
	 */
	public static String generatePath(String courseCode, String courseId, String assignmentId) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseId + "/" + assignmentId + "/";
		
		return path;
	}

	/**
	 * Generates a string representing a path to the directory of a students
	 * submission on an assignment on the filesystem.
	 *
	 * @param courseCode	courses 6 character identifier
	 * @param courseID		courses unique database id
	 * @param assignmentID	assignments unique database id
	 * @param studentID		students unique database id
	 * @return				path to directory
	 */
	public static String generatePath(String courseCode, String courseID,
			String assignmentID, String studentID) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseID + "/" + studentID  + "/" + assignmentID + "/";
		
		return path;
	}

	public static String generatePathFromModel(FeedbackModel model){
		String path = generatePath(
				model.getCourseCode(),
				model.getCourseID(),
				""+model.getAssignmentID(),
				""+model.getStudentID());

		return path;
	}

	/**
	 * Gets the specified video on the fileserver.
	 * @param path 	The path to the file on the fileserver.
	 * @return		The video, in the form of an responseentity.
     */
	public static ResponseEntity<InputStreamResource> getVideo(String path) {
		ResponseEntity<InputStreamResource> responseEntity;
		File video = new File(path);

		try {
			byte[] out = FileCopyUtils.copyToByteArray(video);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "inline; filename=AssignmentVideo");
			responseEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			responseEntity = new ResponseEntity("File not found.", HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			responseEntity = new ResponseEntity("Error getting file.", HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}


	/**
     * starts a stream to student video for a specific assignment at a course.
     * 
     * @parma courseCode the code for the course.
     * @param courseID course id from the database
     * @param userID
     * @return video or null if it doesn't exist. 
     */
	public FileInputStream getStudentVideo(String courseCode, String courseID,
										   String assignmentID, String userID) {
	   String path = FilesystemInterface.generatePath(courseCode, courseID,
			   assignmentID, userID) + FilesystemConstants
			   .SUBMISSION_VIDEO_FILENAME;
	   
	   try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			return null;
		}
   	}

    /**
     * Fetches an assignment's description from the file system.
     * @param courseCode Identifies the course associated with the assignment
     * @param courseId Identifies the instance of the course
     * @param assignmentId Unique code of the assignment
     * @return
     */
    public FileInputStream getAssignmentDescription(String courseCode, String courseId, String assignmentId) {
        String path = FilesystemInterface.generatePath(courseCode, courseId, assignmentId) + FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME;
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.err.println("No description of the assignment was found.\n");
            return null;
        }
    }

	/**
	 * Store the students video for an assignment at a course.
	 * If student folder doesn't exist a folder will be created.
	 *
	 * @param courseCode the code for the course.
	 * @param courseID course id from the database
	 * @param assignmentID from database
	 * @param userID from database
	 * @return true if video was stored successfully
	 */
	public static boolean storeStudentVideo(String courseCode, String courseID,
											String assignmentID, String userID,
											MultipartFile source) {

		String path = FilesystemInterface.generatePath(courseCode, courseID,
				assignmentID, userID);
		try {
			storeFile(source ,path,FilesystemConstants.SUBMISSION_VIDEO_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Store the teacher's feedback video to a student submission.
	 *
	 * @param courseCode the code for the course.
	 * @param courseId course id from the database
	 * @param assignmentId from database
	 * @param userId from database
	 * @return true if video was stored successfully
	 */
	public static boolean storeFeedbackVideo(String courseCode, String courseId,
											String assignmentId, String userId,
									  MultipartFile source) {

		String path = FilesystemInterface.generatePath(courseCode, courseId,
				assignmentId, userId);

		try {
			storeFile(source,path, FilesystemConstants.FEEDBACK_VIDEO_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Store the teacher's feedback text to a student submission.
	 *
	 * @param courseCode the code for the course.
	 * @param courseId course id from the database
	 * @param assignmentId from database
	 * @param userId from database
	 * @return true if video was stored successfully
	 */
	public static boolean storeFeedbackText(FeedbackModel model, MultipartFile source) {
		return storeFeedbackText(
				model.getCourseCode(),
				model.getCourseID(),
				""+ model.getAssignmentID(),
				""+model.getStudentID(),
				source);
	}

	/**
	 * Store the teacher's feedback text to a student submission.
	 *
	 * @param courseCode the code for the course.
	 * @param courseId course id from the database
	 * @param assignmentId from database
	 * @param userId from database
	 * @return true if video was stored successfully
	 */
	public static boolean storeFeedbackText(String courseCode, String courseId,
									 String assignmentId, String userId,
									 MultipartFile source) {

		String path = FilesystemInterface.generatePath(courseCode, courseId,
				assignmentId, userId);

		try {
			storeFile(source,path,FilesystemConstants.FEEDBACK_TEXT_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getFeedbackText(FeedbackModel model) {
		return "hej";
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
	 * Returns the size of a specific video file.
	 * 
     * @param courseCode    courses 6 character identifier
     * @param courseId      courses unique database id
     * @param assignmentId  assignments unique database id
     * @param userId        users unique database id
	 * @return              video file size
	 * @author              Stefan Embretsen
	 */
	public int getVideoFileSize(String courseCode, String courseId,
								String assignmentId, String userId){
	    String path = FilesystemInterface.generatePath(courseCode, courseId, 
	               assignmentId, userId) + FilesystemConstants
	               .SUBMISSION_VIDEO_FILENAME;
	    File f = new File(path);
	    return (int)f.length();
	}


}
