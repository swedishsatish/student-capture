package studentcapture.datalayer.filesystem;


import studentcapture.config.StudentCaptureApplication;

public final class FilesystemConstants {
	public final static String FILESYSTEM_PATH = StudentCaptureApplication.ROOT+"/moose"; //TODO MooseFS directory
	public final static String SUBMISSION_VIDEO_FILENAME = "submission";
	public final static String FEEDBACK_VIDEO_FILENAME = "feedback";
	public final static String FEEDBACK_TEXT_FILENAME = "feedback.xml";
	public final static String ASSIGNMENT_VIDEO_FILENAME = "video";
    public final static String ASSIGNMENT_DESCRIPTION_FILENAME = "description.xml";
}
