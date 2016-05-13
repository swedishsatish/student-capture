package studentcapture.datalayer.database;

import java.util.Map;

/**
 * 
 * @author tfy12hsm
 *
 */
public class Course {
	private String courseId;
	private Integer year;
	private String term;
	private String courseCode;
	private String courseName;
	private String courseDescription;
	private Boolean active;
	
	public Course() {
		
	}
	
	public Course(Map<String, Object> map) {
		parseMap(map);
	}
	
	/**
	 * Constructor that parses a map of database elements.
	 * 
	 * @param map		map of database elements
     * 
     * @author tfy12hsm
	 */
	public void parseMap(Map<String, Object> map) {
		courseId = (String) map.get("CourseId");
		year = (Integer) map.get("Year");
		term = (String) map.get("Term");
		courseCode = (String) map.get("CourseCode");
		courseName = (String) map.get("CourseName");
		courseDescription = (String) map.get("CourseDescription");
		active = (Boolean) map.get("Active");
	}
	
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}
	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}
	/**
	 * @param courseCode the courseCode to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the courseDescription
	 */
	public String getCourseDescription() {
		return courseDescription;
	}
	/**
	 * @param courseDescription the courseDescription to set
	 */
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
}
