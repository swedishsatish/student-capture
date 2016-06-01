package studentcapture.assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.config.StudentCaptureApplicationTests;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class AssignmentIntervallTest extends StudentCaptureApplicationTests {

    private AssignmentDateIntervalls assignmentIntervalls;
    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithFaultyDates() {
        // Faulty date format
        String startDate = "20161019 111212";
        //Set wrong date
        assignmentIntervalls.setStartDate(startDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEndDateIsBeforeStartDate(){
        String startDate = currentDatePlusDaysGenerator(3);
        // enddate is one day before startdate
        String endDate = currentDatePlusDaysGenerator(2);

        assignmentIntervalls.setStartDate(startDate);
        assignmentIntervalls.setEndDate(endDate);
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(formatter);
    }
}
