package assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.assignment.AssignmentDateIntervalls;
import studentcapture.assignment.AssignmentModel;
import studentcapture.assignment.AssignmentVideoIntervall;

import static org.junit.Assert.*;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModelTest {

    private AssignmentModel assignmentModel;

    @Before
    public void setUp() {
        AssignmentVideoIntervall videoIntervall = new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls = new AssignmentDateIntervalls();
        assignmentModel = new AssignmentModel();

        videoIntervall.setMinTimeSeconds(120);
        videoIntervall.setMaxTimeSeconds(300);
        assignmentIntervalls.setStartDate("2016-01-22 15:00:00");
        assignmentIntervalls.setEndDate("2016-01-24 10:00:00");
        assignmentIntervalls.setPublishedDate("2016-01-22 15:00:00");
        assignmentModel.setTitle("Test");
        assignmentModel.setDescription("Info");
        assignmentModel.setVideoIntervall(videoIntervall);
        assignmentModel.setAssignmentIntervall(assignmentIntervalls);
        assignmentModel.setScale("NUMBER_SCALE");
        assignmentModel.setRecap("Recap");

    }

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
    public void descriptionShouldBeInfo() {
        assertEquals("Info", assignmentModel.getDescription());
    }

    @Test
    public void descriptionShouldBeTest1() {
        assignmentModel.setDescription("Test1");
        assertEquals("Test1", assignmentModel.getDescription());
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
    public void CourseIDShouldBe1200() {
        assignmentModel.setCourseID("1200");
        assertEquals("1200", assignmentModel.getCourseID());
    }

    @Test
    public void CourseIDShouldBe2() {
        assignmentModel.setCourseID("2");
        assertEquals("2", assignmentModel.getCourseID());
    }

    @Test
    public void minTimeSecondsShouldBe120() {
        assertEquals(120, assignmentModel.getVideoIntervall().getMinTimeSeconds());
    }

    @Test
    public void minTimeSecondsShouldBe150() {
        assignmentModel.getVideoIntervall().setMinTimeSeconds(150);
        assertEquals(150, assignmentModel.getVideoIntervall().getMinTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe300() {
        assertEquals(300, assignmentModel.getVideoIntervall().getMaxTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe290() {
        assignmentModel.getVideoIntervall().setMaxTimeSeconds(290);
        assertEquals(290, assignmentModel.getVideoIntervall().getMaxTimeSeconds());
    }

    /*@Test
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

    /*@Test(expected = DateTimeParseException.class)
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
    }  */

    /*@Test(expected = InputMismatchException.class)
    public void startDateShouldNotBeAfterEndDate() {
        assignmentModel.setStartDate("2015-01-22 10:00:00");
        assignmentModel.setEndDate("2015-01-20 10:00:00");
    }

    @Test(expected = InputMismatchException.class)
    public void publishDateShouldNotBeAfterStartDate() {
        assignmentModel.setStartDate("2015-01-19 10:00:00");
        assignmentModel.setPublished("2015-01-20 10:00:00");
    }*/
}
