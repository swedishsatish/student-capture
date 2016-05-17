package studentcapture.assignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class AssignmentValidator {

    public static void validate(AssignmentModel assignmentModel) {
        LocalDateTime startDateTime, endDateTime, publishedDate;

        //Check dates
        startDateTime = convertStringToLDTFormat(assignmentModel.getStartDate(), "startDate is not in format" +
                " YYYY-MM-DD HH:MI:SS");

        endDateTime = convertStringToLDTFormat(assignmentModel.getEndDate(), "endDate is not in " +
                "format YYYY-MM-DD HH:MI:SS");

        checkIfTime1IsBeforeTime2(startDateTime, endDateTime, "Start date" +
                " must be before the end date");

        if (assignmentModel.getPublished() != null) {
            LocalDateTime currentDate = LocalDateTime.now();

            publishedDate = convertStringToLDTFormat(assignmentModel.getPublished(), "published is" +
                    " not in format YYYY-MM-DD HH:MI:SS");

            checkIfTime1IsBeforeTime2(currentDate, publishedDate, "Published" +
                    " date must be after the current date");
        }

        // Check time
        validateMinMaxTime(assignmentModel.getMinTimeSeconds(), assignmentModel.getMaxTimeSeconds());


    }

    private static LocalDateTime convertStringToLDTFormat(String timeDateString,
                                                   String errorMessage)
            throws IllegalArgumentException {
        LocalDateTime dateTime;

        try {
            dateTime = checkIfCorrectDateTimeFormat(timeDateString);
        } catch (DateTimeParseException e){
            throw new IllegalArgumentException(errorMessage);
        }

        return dateTime;
    }

    private static LocalDateTime checkIfCorrectDateTimeFormat(String dateTime)
            throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    private static void checkIfTime1IsBeforeTime2(LocalDateTime t1, LocalDateTime t2,
                                           String errorMessage)
            throws IllegalArgumentException {
        if (t2.isBefore(t1)){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void validateMinMaxTime(int minTime, int maxTime)
            throws IllegalArgumentException{
        if (minTime >= maxTime) {
            throw new IllegalArgumentException("minTime must be less than " +
                    "maxTime");
        }
        if (minTime < 0) {
            throw new IllegalArgumentException("minTime must be greater or " +
                    "equal to 0");
        }
        if (maxTime <= 0) {
            throw new IllegalArgumentException("maxTime must be greater " +
                    "than 0");
        }
    }
}
