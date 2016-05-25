package studentcapture.course;

import java.util.Map;
import java.util.Objects;

/**
 * CourseModel contains information related to a course stored on the database.
 * It is used to transport this information between the front end and the 
 * database.
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
	
	/**
	 * Constructor. Creates an empty CourseModel.
	 */
	public CourseModel() {
	}
	
	/**
	 * Constructor. Creates a CourseModel using data from a jdbcTemplate 
	 * query.
	 * 
	 * @param map		map of data gotten from query
	 */
	public CourseModel(Map<String, Object> map) {
		parseMap(map);
	}
	
	/**
	 * Parses a map of database elements and adds them to the 
	 * 
	 * @param map		map of database elements
	 */
	private void parseMap(Map<String, Object> map) {
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
	 * @param active the active to set :^)
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CourseModel that = (CourseModel) o;

		if (!courseId.equals(that.courseId)) return false;
		if (!year.equals(that.year)) return false;
		if (!term.equals(that.term)) return false;
		if (courseName != null ? !courseName.equals(that.courseName) : that.courseName != null) return false;
		if (courseDescription != null ? !courseDescription.equals(that.courseDescription) : that.courseDescription != null)
			return false;
		if (!active.equals(that.active)) return false;
		if (!initialTeacherId.equals(that.initialTeacherId)) return false;
		return errorCode.equals(that.errorCode);

	}

	@Override
	public int hashCode() {
		int result = courseId.hashCode();
		result = 31 * result + year.hashCode();
		result = 31 * result + term.hashCode();
		result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
		result = 31 * result + (courseDescription != null ? courseDescription.hashCode() : 0);
		result = 31 * result + active.hashCode();
		result = 31 * result + initialTeacherId.hashCode();
		result = 31 * result + errorCode.hashCode();
		return result;
	}
}
