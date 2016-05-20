package studentcapture.datalayer.filesystem;


import studentcapture.config.StudentCaptureApplication;

public final class FilesystemConstants {
	public final static String FILESYSTEM_PATH = StudentCaptureApplication.ROOT+"/moose"; //TODO MooseFS directory
	public final static String SUBMISSION_VIDEO_FILENAME = "submission.webm";
	public final static String FEEDBACK_VIDEO_FILENAME = "feedback.webm";
	public final static String FEEDBACK_TEXT_FILENAME = "feedback.xml";
	public final static String ASSIGNMENT_VIDEO_FILENAME = "assignment.webm";
    public final static String ASSIGNMENT_DESCRIPTION_FILENAME = "description.xml";
	public final static String ASSIGNMENT_RECAP_FILENAME = "recap.txt";
}
