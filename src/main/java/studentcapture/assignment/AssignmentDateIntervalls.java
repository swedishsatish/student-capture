package studentcapture.assignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class AssignmentDateIntervalls {

    private LocalDateTime startDate;
    private boolean startDateIsSet;
    private LocalDateTime endDate;
    private boolean endDateIsSet;
    private LocalDateTime publishedDate;
    private boolean publishedDateIsSet;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    public AssignmentDateIntervalls() {
        startDateIsSet = false;
        endDateIsSet = false;
        publishedDateIsSet = false;
    }

    public void setStartDate(String startDate) throws DateTimeParseException, IllegalArgumentException {
        startDateIsSet = true;
        this.startDate = LocalDateTime.parse(startDate, FORMATTER);
        validate();
    }

    public String getStartDate() {
        return FORMATTER.format(startDate);
    }

    public void setEndDate(String endDate) throws DateTimeParseException, IllegalArgumentException {
        endDateIsSet = true;
        this.endDate = LocalDateTime.parse(endDate, FORMATTER);
        validate();
    }

    public String getEndDate() {
        return FORMATTER.format(endDate);
    }

    public void setPublishedDate(String publishedDate) throws DateTimeParseException, IllegalArgumentException {
        if (publishedDate != null) {
            publishedDateIsSet = true;
            this.publishedDate = LocalDateTime.parse(publishedDate, FORMATTER);
            validate();
        } else {
            this.publishedDateIsSet = false;
            this.publishedDate = null;
        }
    }

    public String getPublishedDate() {
        if (publishedDateIsSet) {
            return FORMATTER.format(publishedDate);
        } else {
            return null;
        }
    }

    private void validate() throws IllegalArgumentException {
        if (publishedDateIsSet && startDateIsSet) {
            if (publishedDate.isAfter(startDate)) {
                throw new IllegalArgumentException("Publish date can't be after start date");
            }
        }

        if (startDateIsSet && endDateIsSet) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date can't be after end date");
            }
        }

        if (startDateIsSet) {
            LocalDateTime currentDate = LocalDateTime.now();
            if(isDatesEqual(startDate, currentDate)) {
                throw new IllegalArgumentException("Start date can't be todays date");
            }
        }
    }

    private boolean isDatesEqual(LocalDateTime first, LocalDateTime second) {
        int firstDay = first.getDayOfMonth();
        int firstMonth = first.getMonthValue();
        int firstYear = first.getYear();
        int secondDay = second.getDayOfMonth();
        int secondMonth = second.getMonthValue();
        int secondYear = second.getYear();

        return ((firstDay == secondDay) && (firstMonth == secondMonth) && (firstYear == secondYear));
    }
}
