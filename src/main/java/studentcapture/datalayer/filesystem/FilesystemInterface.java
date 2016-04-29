package studentcapture.datalayer.filesystem;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

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
	public static String generatePath(String courseCode, String courseId, int assignmentId) {
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
	public static String generatePath(String courseCode, String courseId,
			int assignmentId, int studentId) {
		String path = FilesystemConstants.FILESYSTEM_PATH + "/" + courseCode
				+ "/" + courseId + "/" + assignmentId + "/" + studentId + "/";
		
		return path;
	}
	
	/**
     * starts a stream to student video for a specific assignment at a course.
     * 
     * @param courseCode the code for the course.
     * @param courseId course id from the database
     * @param userId
     * @return video or null if it doesn't exist. 
     */
	public FileInputStream getStudentVideo(String courseCode, String courseId,
		   int assignmentId, int userId) {
	   String path = FilesystemInterface.generatePath(courseCode, courseId, 
			   assignmentId, userId) + FilesystemConstants
			   .SUBMISSION_VIDEO_FILENAME;
	   
	   try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO
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
    public FileInputStream getAssignmentDescription(String courseCode, String courseId, int assignmentId) {
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
     * @param courseId course id from the database
     * @param assignmentId from database
     * @param userId from database
     * @return true if video was stored successfully
     */
	public static boolean storeStudentVideo(String courseCode, String courseId,
		   int assignmentId, int userId, File source) {

        String path = FilesystemInterface.generatePath(courseCode, courseId,
			   assignmentId, userId) + FilesystemConstants
			   .SUBMISSION_VIDEO_FILENAME;

        try {
            storeFile(source,path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
	}


    /**
     * Stores file at given path, if path doesn't exist it will be
     * created.
     *
     * @throws IOException in case that it can't create a folder at given path
     * @param source the video file to be stored
     * @param des destination for the video file
     */
    private static void storeFile(File source, String des) throws IOException {

        File desFile = new File(des+"/" + source.getName());

        FileOutputStream outStream = new FileOutputStream(desFile);
        FileInputStream inStream   = new FileInputStream(source);

        FileChannel inputChannel   = inStream.getChannel();
        FileChannel outputChannel  = outStream.getChannel();

        outputChannel.transferFrom(inputChannel,0,inputChannel.size());
    }
}
