package studentcapture.datalayer.database;

import java.util.List;

/**
 * Created by c12ton on 4/26/16.
 */
public class Assignment {

        public int createAssignment(String courseID, String assignmentTitle,
                                    String startDate, String endDate, int minTime, int maxTime,
                                    boolean published){
            //TODO
            return 0;
        }

        public List<Object> getAssignmentInfo(int assignmentID){
            //TODO
            return null;
        }

        public boolean updateAssignment(String assignmentID, String assignmentTitle,
                                        String startDate, String endDate, int minTime, int maxTime,
                                        boolean published){
            //TODO
            return true;
        }

        public boolean removeAssignment(String assignmentID){
            //TODO
            return true;
        }

}
