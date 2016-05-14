package studentcapture.model;

import java.util.Map;

/**
 * 
 * @author tfy12hsm
 *
 */
public class Participant {
    private Integer userId;
    private String courseId;
    private String function;
    
    public Participant() {
    }
    
    public Participant(Map<String, Object> map) {
    	parseMap(map);
    }
    
    /**
     * Parses values from a map of database elements into class members.
     * 
     * @param map		map of database elements
     */
    public void parseMap(Map<String, Object> map) {
    	userId = (Integer) map.get("UserId");
		courseId = (String) map.get("CourseId");
		function = (String) map.get("Function");
    }
    
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
}
