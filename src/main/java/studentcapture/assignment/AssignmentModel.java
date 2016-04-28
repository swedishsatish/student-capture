package studentcapture.assignment;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModel {

    private String title;
    private String info;
    private int minTimeSeconds;
    private int maxTimeSeconds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AssignmentModel(String title, String info, int minTimeSeconds, int maxTimeSeconds, String startDate,
                           String endDate) throws InputMismatchException
    {
        this.title = title;
        this.info = info;
        this.minTimeSeconds = minTimeSeconds;
        this.maxTimeSeconds = maxTimeSeconds;
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);

        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
        validateStartEndTime(this.startDate, this.endDate);
    }

    public AssignmentModel() {
        this.title = "Defualt Assignment";
        this.info = "Defualt Info";
        this.minTimeSeconds = 0;
        this.maxTimeSeconds = 0;
        this.startDate = LocalDateTime.parse("2000-10-12T10:00");
        this.endDate = LocalDateTime.parse("2000-10-13T10:00");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getMinTimeSeconds() {
        return minTimeSeconds;
    }

    public void setMinTimeSeconds(int minTimeSeconds) throws  InputMismatchException {
        this.minTimeSeconds = minTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public int getMaxTimeSeconds() {
        return maxTimeSeconds;
    }

    public void setMaxTimeSeconds(int maxTimeSeconds) throws  InputMismatchException {
        this.maxTimeSeconds = maxTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = LocalDateTime.parse(endDate);
        validateStartEndTime(this.startDate, this.endDate);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = LocalDateTime.parse(startDate);
        //validateStartEndTime(this.startDate, this.endDate);
    }

    private void validateStartEndTime(LocalDateTime startDate, LocalDateTime endDate) throws InputMismatchException {
        if (startDate.isAfter(endDate)) {
            throw new InputMismatchException("Start Time is after end day, Start time was " + startDate +
            " and end time " + endDate);
        }
    }

    private void validateMinMaxTimeSeconds(int minTimeSeconds, int maxTimeSeconds) {
        if ((minTimeSeconds > maxTimeSeconds) && (maxTimeSeconds != 0)) {
            throw new InputMismatchException("Minimum time can't be larger than max time, input was, min: " +
                    minTimeSeconds + " max: " + maxTimeSeconds);
        }
    }
}
