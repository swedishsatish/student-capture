package studentcapture.datalayer.filesystem;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;

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
	 * @param courseID		courses unique database id
	 * @param assignmentId	assignments unique database id
	 * @param studentID		students unique database id
	 * @return				path to directory
	 */
	public static String generatePath(String courseCode, String courseID,
			String assignmentId, String studentID) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseID + "/" + assignmentId + "/" + studentID + "/";
		
		return path;
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
											MultipartFile source, String filename) {

		String path = FilesystemInterface.generatePath(courseCode, courseID,
				assignmentID, userID) + FilesystemConstants
				.SUBMISSION_VIDEO_FILENAME;

		try {
			storeFile(source ,filename,path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	/**
	 * Stores a file at given path, if path doesn't exist it will be
	 * created.
	 *
	 * @throws IOException in case that it can't create a folder at given path
	 * @param source the video file to be stored
	 * @param des destination for the video file
	 */
	private static void storeFile(MultipartFile source, String filename, String des) throws IOException {
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
