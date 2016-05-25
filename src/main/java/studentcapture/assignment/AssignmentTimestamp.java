package studentcapture.assignment;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class that is used to check how long a specific student has been doing an assignment.
 * If the student has taken too long time, the teacher will be notified.
 * Created by Stefan Embretsen on 2016-05-16.
 */
@RestController
@RequestMapping("/assignmentTimestamp")
public class AssignmentTimestamp {

    //Used to give the student a little more time, in case of lag.
    final static private double allowedTimeMultiplyer = 2;
    //Folder namn for temporary files.
    final static private String tempFolder = "tmpAssignments";
    //Path to the temporary folder.
    final static private String path = System.getProperty("user.dir")+"/"+tempFolder+"/";
    //Number of days the assignment timestamps will be stored.
    final static private int tempFileTime = 2;

    /**
     * Creates a file with a timestamp from when the assignment started.
     * @param   studentID specific students ID.
     * @param   assignmentID specific assignments ID.
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void startAssignment(@RequestParam("studentID")String studentID,
                                @RequestParam("assignmentID") String assignmentID){
        try {
            File dir = new File(tempFolder);
            if(!dir.exists()){
                if(dir.mkdir()){
                    notifySubmission(false, 0, studentID, assignmentID);
                }
            }
            FileOutputStream FW = new FileOutputStream(path +studentID+assignmentID, false);
            Date now = new Date();
            Long longTime = now.getTime();
            FW.write(longTime.toString().getBytes());
            FW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Checks if the time the assignment has been open is lower than the video
     * length*allowedTimeMultiplyer.
     * If it's lower true will be returned, else false.
     * @param   videoLength Length of the question video.
     * @param   answerLength length of the answer video.
     * @param   studentID specific students ID.
     * @param   assignmentID specific assignments ID.
     * @return  whether the time is within the limit or not.
     */
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public HttpStatus registerTime(@RequestParam("videoLength")int videoLength,
                                   @RequestParam("answerLength")int answerLength,
                                   @RequestParam("studentID")String studentID,
                                   @RequestParam("assignmentID")String assignmentID){
        try {

            int assignmentTime = (int) getAssignmentTime(path + studentID+assignmentID);
            File assignmentFile = new File(path+studentID+assignmentID);
            if(!assignmentFile.delete()){
                notifySubmission(false, 0, studentID, assignmentID);
            }
            cleanUp(studentID, assignmentID);
            //Will be equal to the the the student spent on watching the video.
            int watchedVideoTime = assignmentTime - answerLength;

            if(watchedVideoTime < videoLength*allowedTimeMultiplyer){
                notifySubmission(true, assignmentTime, studentID, assignmentID );
                return HttpStatus.ACCEPTED;
            }else{
                notifySubmission(false, assignmentTime, studentID, assignmentID );
                return HttpStatus.NOT_ACCEPTABLE;
            }
        } catch (IOException e) {
            notifySubmission(false, -1, studentID, assignmentID );
            return HttpStatus.NOT_FOUND;
        }
    }

    /**
     * Returns the number of milliseconds passed since the assignment was started.
     * @param path path to the file which contains the time.
     * @return assignment time.
     */
    private long getAssignmentTime(String path) throws IOException {
        String startTime = new String(Files.readAllBytes(Paths.get(path)));
        Date now = new Date();
        return now.getTime()-Long.parseLong(startTime);
    }


    /**
     * Writes to submission info about the students time spent on the assignment.
     * @param inTime boolean if the assignment was handed in time or not.
     * @param assignmentTime Assignment time.
     * @param studentID specific students ID.
     * @param assignmentID specific assignments ID.
     */
    private void notifySubmission(boolean inTime, int assignmentTime, String studentID, String assignmentID){
        Date now = new Date();
        String submissionText;
        if(assignmentTime == -1){
            submissionText = getTimeStamp() +
                    ": Turned in assignment but information about length was lost.";
        }else if(inTime){
            submissionText = getTimeStamp() +
                    ": Turned in assignment and was within the time limit. Assignment time: "
                    + formatMilliSeconds(assignmentTime);
        }else{
            submissionText = getTimeStamp() +
                    ": Turned in assignment but took longer time than allowed. Assignment time: "
                    + formatMilliSeconds(assignmentTime);
        }
        //TODO: Send submissionText to submission.
        System.out.println(submissionText);
    }


    /**
     * Creates and returns a timestamp formated to "yyyy.MM.dd.HH.mm.ss" of the current time.
     * @return timestamp.
     */
    private String getTimeStamp(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }

    /**
     * Formats the number of milliseconds spent on the assignment to the form 'XX mins. XX seconds.'.
     * @param time milliseconds that will be formated.
     * @return formated time.
     */
    private String formatMilliSeconds(int time){
        time = time/1000;
        int seconds = time%60;
        time = time/60;
        return time + " mins. " + seconds + " seconds.";
    }


    /**
     * Searches the temporary directory and deletes files older than tempFileTime days.
     * If the temporary directory is empty, it will be deleted.
     */
    private void cleanUp(String studentID, String assignmentID){
        File tempDir = new File(tempFolder);
        Date now = new Date();
        Long currentTime = now.getTime();
        boolean dirIsEmpty = true;
        File[] files = tempDir.listFiles();
        assert files != null;
        for (File file : files) {
            if((currentTime-file.lastModified()) > (tempFileTime * 24 * 60 * 60 * 1000)){
                if(!file.delete()){
                    notifySubmission(false, 0, studentID, assignmentID);
                }
            }else{
                dirIsEmpty = false;
            }
        }

        if(dirIsEmpty){
            if(!tempDir.delete()){
                notifySubmission(false, 0, studentID, assignmentID);
            }
        }
    }


}