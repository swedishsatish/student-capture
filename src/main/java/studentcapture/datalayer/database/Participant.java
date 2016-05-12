package studentcapture.datalayer.database;

import java.util.Map;

public class Participant {
    private Integer userId;
    private String courseId;
    private String function;
    
    public Participant() {
    }
    
    public Participant(Map<String, Object> map) {
    	parseMap(map);
    }
    
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
