package model;

import org.junit.Test;
import studentcapture.assignment.AssignmentModel;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModelTest {

    private AssignmentModel assignmentModel = new AssignmentModel("Test", "Info", 120, 300, "2016-01-22T15:00", "2016-01-24T10:00", true);

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
    public void publishedShouldBeTrue() {
        assertTrue(assignmentModel.getPublished());
    }

    @Test
    public void publishedShouldBeFalse() {
        assignmentModel.setPublished(false);
        assertFalse(assignmentModel.getPublished());
    }

    @Test
    public void CourseIDShouldBe1() {
        assertEquals(1, assignmentModel.getCourseID());
    }

    @Test
    public void CourseIDShouldBe2() {
        assignmentModel.setCourseID(2);
        assertEquals(2, assignmentModel.getCourseID());
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
        new AssignmentModel("Test", "info", 120, 300, "2015-01-20T10:00", "2015-01-22T10:00", true);
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldThrowDateTimeParseExceptionBecauseNotValidMonth() {
        new AssignmentModel("Test", "info", 120, 300, "2015-20-20T10:00", "2015-01-20T10:00", true);
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldBe28DaysInFeb() {
        new AssignmentModel("Test", "info", 120, 300, "2015-02-29T10:00", "2015-01-20T10:00", true);
    }

    @Test(expected = DateTimeParseException.class)
    public void hourShouldBeUnder23() {
        new AssignmentModel("Test", "info", 120, 300, "2015-01-20T24:00", "2015-01-20T10:00", true);
    }

    @Test(expected = DateTimeParseException.class)
    public void minuteShouldBeUnder59() {
        new AssignmentModel("Test", "info",  120, 300, "2015-01-20T10:60", "2015-01-20T10:00", true);
    }

    @Test(expected = InputMismatchException.class)
    public void minTimeShouldBeSmallerThanMaxTime() {
        new AssignmentModel("Test", "info", 300, 120, "2015-01-20T10:00", "2015-01-20T10:00", true);
    }

    @Test
    public void shouldNotThrowExceptionSinceMaxTimeIs0() {
        assignmentModel.setMaxTimeSeconds(0);
        assignmentModel.setMinTimeSeconds(120);
        assertEquals(120, assignmentModel.getMinTimeSeconds());
    }

    @Test(expected = InputMismatchException.class)
    public void startDateShouldNotBeAfterEndDate() {
        new AssignmentModel("Test", "info", 300, 120, "2015-01-22T10:00", "2015-01-20T10:00", true);
    }
}
