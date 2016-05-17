package assignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class AssignmentIntervall {

    private LocalDateTime startDate;
    private boolean startDateIsSet;
    private LocalDateTime endDate;
    private boolean endDateIsSet;
    private LocalDateTime publishedDate;
    private boolean publishedDateIsSet;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    public AssignmentIntervall() {
        startDateIsSet = false;
        endDateIsSet = false;
        publishedDateIsSet = false;
    }

    public void setStartDate(String startDate) throws DateTimeParseException {
        startDateIsSet = true;
        this.startDate = LocalDateTime.parse(startDate, FORMATTER);
    }

    public String getStartDate() {
        return FORMATTER.format(startDate);
    }

    public void setEndDate(String endDate) throws DateTimeParseException {
        endDateIsSet = true;
        this.endDate = LocalDateTime.parse(endDate, FORMATTER);
    }

    public String getEndDate() {
        return FORMATTER.format(endDate);
    }

    public void setPublishedDate(String publishedDate) throws DateTimeParseException {
        publishedDateIsSet = true;
        this.publishedDate = LocalDateTime.parse(publishedDate, FORMATTER);
    }

    public String getPublishedDate() {
        if (publishedDateIsSet) {
            return FORMATTER.format(publishedDate);
        }
    }

    private void validate() {

    }
}
