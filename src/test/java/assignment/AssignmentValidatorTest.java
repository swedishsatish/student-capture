package assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.assignment.AssignmentModel;
import studentcapture.assignment.AssignmentValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class AssignmentValidatorTest {

    private AssignmentModel am;
    private String courseID = "UA502";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() {
        am = new AssignmentModel("PVT", //Title
                "", // Info
                180, // MinTime
                360, // MaxTime
                currentDatePlusDaysGenerator(2), // StartDate
                currentDatePlusDaysGenerator(3), // EndDate
                currentDatePlusDaysGenerator(1), // PublishDate
                "U_O_K_G", // GradeScale
                ""); // Recap
        am.setCourseID(courseID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenPublishdateIsBeforeCurrentdate(){
        //Faulty publishdate
        LocalDateTime publishDateTime = LocalDateTime.now().minusDays(1);
        String published = publishDateTime.format(formatter);
        am.setPublished(published);

        AssignmentValidator.validate(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithFaultyDates() throws Exception {
        String startDate = "20161019 111212"; // Faulty date format
        //Set wrong date

        am.setStartDate(startDate);
        AssignmentValidator.validate(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEndDateIsBeforeStartDate(){
        String startDate = currentDatePlusDaysGenerator(3);
        String endDate = currentDatePlusDaysGenerator(2); // enddate is one day
        // before startdate

        am.setStartDate(startDate);
        am.setEndDate(endDate);
        AssignmentValidator.validate(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeMinGreaterMaxTime() throws Exception {
        int minTime = 300; // Faulty when minTime is greater than maxTime
        int maxTime = 210;

        am.setMinTimeSeconds(minTime);
        am.setMaxTimeSeconds(maxTime);
        AssignmentValidator.validate(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeNegative() throws Exception {
        int minTime = -1; // Faulty when time is negative

        am.setMinTimeSeconds(minTime);
        AssignmentValidator.validate(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeZero() throws Exception {
        int maxTime = 0; //Faulty when time is zero

        am.setMaxTimeSeconds(maxTime);
        AssignmentValidator.validate(am);
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(formatter);
    }
}
