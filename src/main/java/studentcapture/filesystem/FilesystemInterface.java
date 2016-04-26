package studentcapture.filesystem;

import java.io.FileInputStream;

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
	public static String generatePath(String courseCode, int courseId, int assignmentId) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseId + "/" + assignmentId + "/";
		
		return path;
	}
	
	/**
	 * Generates a string representing a path to the directory of a students 
	 * submission on an assignment on the filesystem.
	 * 
	 * @param courseCode	courses 6 character identifier
	 * @param courseId		courses unique database id
	 * @param assignmentId	assignments unique database id
	 * @param studentId		students unique database id
	 * @return				path to directory
	 */
	public static String generatePath(String courseCode, int courseId, 
			int assignmentId, int studentId) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseId + "/" + assignmentId + "/" + studentId + "/";
		
		return path;
	}
	
	/**
     * starts a stream to student video for a specific assignment at a course.
     * 
     * @parma courseCode the code for the course.
     * @param courseID course id from the database
     * @param userId
     * @return video or null if it doesn't exist. 
     */
   public FileInputStream getStudentVideo(String courseCode, int courseId, 
		   int assignmentID, int userID) {
	   return null;
   }
	
}
