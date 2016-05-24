package studentcapture.course.invite;

import studentcapture.course.CourseModel;

/**
 * 
 * @author tfy12hsm
 *
 */
public class InviteModel {
	private String hex;
	private CourseModel course;
	/**
	 * @return the hex
	 */
	public String getHex() {
		return hex;
	}
	/**
	 * @param hex the hex to set
	 */
	public void setHex(String hex) {
		this.hex = hex;
	}
	/**
	 * @return the course
	 */
	public CourseModel getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(CourseModel course) {
		this.course = course;
	}
}
