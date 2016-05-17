package assignment;

import org.junit.Test;
import studentcapture.assignment.AssignmentModel;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

import static org.junit.Assert.*;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModelTest {

    private AssignmentModel assignmentModel = new AssignmentModel("Test", "Info", 120, 300, "2016-01-22 15:00:00",
            "2016-01-24 10:00:00", "2016-01-22 15:00:00", "NUMBER_SCALE", "Recap");

    @Test
    public void titleShouldBeTest() {
        assertEquals("Test", assignmentModel.getTitle());
    }

    @Test
    public void titleShouldBeTest1() {
        assignmentModel.setTitle("Test1");
        assertEquals("Test1", assignmentModel.getTitle());
    }

    @Test
    public void infoShouldBeInfo() {
        assertEquals("Info", assignmentModel.getInfo());
    }

    @Test
    public void infoShouldBeTest1() {
        assignmentModel.setInfo("Test1");
        assertEquals("Test1", assignmentModel.getInfo());
    }

    @Test
    public void recapShouldBeRecap() {
        assertEquals("Recap", assignmentModel.getRecap());
    }

    @Test
    public void recapShouldBeTest1() {
        assignmentModel.setRecap("Test1");
        assertEquals("Test1", assignmentModel.getRecap());
    }

    @Test
    public void gradeScaleShouldBeNumberScale() {
        assertEquals("NUMBER_SCALE", assignmentModel.getScale());
    }

    @Test
    public void gradeScaleShouldBeU_O_K_G() {
        assignmentModel.setScale("U_O_K_G");
        assertEquals("U_O_K_G", assignmentModel.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseInvalidGradeScale() {
        assignmentModel.setScale("InvalidGradeScale");
    }

    @Test
    public void CourseIDShouldBe1000() {
        assertEquals("1000", assignmentModel.getCourseID());
    }

    @Test
    public void CourseIDShouldBe2() {
        assignmentModel.setCourseID("2");
        assertEquals("2", assignmentModel.getCourseID());
    }

    @Test
    public void minTimeSecondsShouldBe120() {
        assertEquals(120, assignmentModel.getMinTimeSeconds());
    }

    @Test
    public void minTimeSecondsShouldBe150() {
        assignmentModel.setMinTimeSeconds(150);
        assertEquals(150, assignmentModel.getMinTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe300() {
        assertEquals(300, assignmentModel.getMaxTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe290() {
        assignmentModel.setMaxTimeSeconds(290);
        assertEquals(290, assignmentModel.getMaxTimeSeconds());
    }

    @Test
    public void shouldNotThrowDateTimeParseException() {
        new AssignmentModel("Test", "info", 120, 300, "2015-01-20 10:00:00", "2015-01-22 10:00:00",
                "2015-01-20 10:00:00", "NUMBER_SCALE", "Recap");
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldThrowDateTimeParseExceptionBecauseNotValidMonth() {
        new AssignmentModel("Test", "info", 120, 300, "2015-20-20 10:00:00", "2015-01-20 10:00:00",
                "2015-01-20 10:00:00", "NUMBER_SCALE", "Recap");
    }

    /*@Test(expected = DateTimeParseException.class)
    public void shouldBe28DaysInFeb() {
        AssignmentModel a = new AssignmentModel("Test", "info", 120, 300, "2015-02-30 10:00:00", "2015-03-20 10:00:00",
                "2015-01-20 10:00:00", "NUMBER_SCALE", "Recap");
        System.out.println(a.getStartDate());
    }*/

    @Test(expected = DateTimeParseException.class)
    public void hourShouldBeUnder23() {
        new AssignmentModel("Test", "info", 120, 300, "2015-01-20 25:00:00", "2015-01-20 10:00:00",
                "2015-01-20 10:00:00", "NUMBER_SCALE", "Recap");
    }

    @Test(expected = DateTimeParseException.class)
    public void minuteShouldBeUnder59() {
        new AssignmentModel("Test", "info",  120, 300, "2015-01-20 10:60:00", "2015-01-20 10:00:00",
                "2015-01-20 10:00:00", "NUMBER_SCALE", "Recap");
    }

    @Test(expected = InputMismatchException.class)
    public void minTimeShouldBeSmallerThanMaxTime() {
        assignmentModel.setMaxTimeSeconds(60);
        assignmentModel.setMinTimeSeconds(120);
    }

    @Test
    public void shouldNotThrowExceptionSinceMaxTimeIs0() {
        assignmentModel.setMaxTimeSeconds(0);
        assignmentModel.setMinTimeSeconds(120);
        assertEquals(120, assignmentModel.getMinTimeSeconds());
    }

    @Test(expected = InputMismatchException.class)
    public void startDateShouldNotBeAfterEndDate() {
        assignmentModel.setStartDate("2015-01-22 10:00:00");
        assignmentModel.setEndDate("2015-01-20 10:00:00");
    }

    @Test(expected = InputMismatchException.class)
    public void publishDateShouldNotBeAfterStartDate() {
        assignmentModel.setStartDate("2015-01-19 10:00:00");
        assignmentModel.setPublished("2015-01-20 10:00:00");
    }
}
