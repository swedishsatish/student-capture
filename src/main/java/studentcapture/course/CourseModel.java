package studentcapture.course;

import java.util.Map;

/**
 * 
 * @author tfy12hsm
 *
 */
public class CourseModel {
	private Integer courseId;
	private Integer year;
	private String term;
	private String courseName;
	private String courseDescription;
	private Boolean active;
	private Integer initialTeacherId;
	private Integer errorCode;
	
	public CourseModel() {
		
	}
	
	public CourseModel(Map<String, Object> map) {
		parseMap(map);
	}
	
	/**
	 * Parses a map of database elements and adds them to the 
	 * 
	 * @param map		map of database elements
     * 
     * @author tfy12hsm
	 */
	public void parseMap(Map<String, Object> map) {
		courseId = (Integer) map.get("CourseId");
		year = (Integer) map.get("Year");
		term = (String) map.get("Term");
		try {
			courseName = (String) map.get("CourseName");
		} catch (NullPointerException e) {
			courseName = null;
		}
		try {
			courseDescription = (String) map.get("CourseDescription");
		} catch (NullPointerException e) {
			courseDescription = null;
		}
		active = (Boolean) map.get("Active");
	}
	
	/**
	 * @return the courseId
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Integer courseId) {
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
	
	public Integer getInitialTeacherId() {
		return initialTeacherId;
	}
	
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
}
