package assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.assignment.AssignmentDateIntervalls;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by root on 5/17/16.
 */
public class AssignmentIntervallTest {

    private AssignmentDateIntervalls assignmentIntervalls;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() {
        assignmentIntervalls = new AssignmentDateIntervalls();
        assignmentIntervalls.setStartDate(currentDatePlusDaysGenerator(2));
        assignmentIntervalls.setEndDate(currentDatePlusDaysGenerator(3));
        assignmentIntervalls.setPublishedDate(currentDatePlusDaysGenerator(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenPublishdateIsBeforeCurrentdate(){
        //Faulty publishdate
        String published = currentDatePlusDaysGenerator(4);
        assignmentIntervalls.setPublishedDate(published);
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldThrowWhenCreateWithFaultyDates() throws Exception {
        String startDate = "20161019 111212"; // Faulty date format

        //Set wrong date
        assignmentIntervalls.setStartDate(startDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEndDateIsBeforeStartDate(){
        String startDate = currentDatePlusDaysGenerator(3);
        String endDate = currentDatePlusDaysGenerator(2); // enddate is one day before startdate

        assignmentIntervalls.setStartDate(startDate);
        assignmentIntervalls.setEndDate(endDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenStartDateIsTodaysDate() {
        String startDate = LocalDateTime.now().format(formatter);

        assignmentIntervalls.setStartDate(startDate);
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(formatter);
    }
}
