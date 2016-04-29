package model;

import org.junit.Test;
import studentcapture.assignment.AssignmentModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModelTest {

    private AssignmentModel assignmentModel = new AssignmentModel("Test", "Info", 120, 300, "2016-01-22T15:00", "2016-01-24T10:00");

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
    public void yearShouldBe2016() {
        assertEquals(2016, assignmentModel.getStartDate().getYear());
    }

    @Test
    public void yearShouldBe2015() {
        assignmentModel.setStartDate("2015-10-12T12:00");
        assertEquals(2015, assignmentModel.getStartDate().getYear());
    }

    @Test
    public void monthShouldBeJan() {
        assertEquals(1, assignmentModel.getStartDate().getMonthValue());
    }

    @Test
    public void monthShouldBeFeb() {
        assignmentModel.setStartDate("2015-02-12T12:00");
        assertEquals(2, assignmentModel.getStartDate().getMonthValue());
    }

    @Test
    public void dayShouldBe22() {
        assertEquals(22, assignmentModel.getStartDate().getDayOfMonth());
    }

    @Test
    public void dayShouldBe23() {
        assignmentModel.setStartDate("2015-02-23T12:00");
        assertEquals(23, assignmentModel.getStartDate().getDayOfMonth());
    }

    @Test
    public void hourShouldBe15() {
        assertEquals(15, assignmentModel.getStartDate().getHour());
    }

    @Test
    public void hourShouldBe16() {
        assignmentModel.setStartDate("2015-02-23T16:00");
        assertEquals(16, assignmentModel.getStartDate().getHour());
    }

    @Test
    public void minuteShouldBe0() {
        assertEquals(0, assignmentModel.getStartDate().getMinute());
    }

    @Test
    public void minuteShouldBe30() {
        assignmentModel.setStartDate("2015-02-23T16:30");
        assertEquals(30, assignmentModel.getStartDate().getMinute());
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

    @Test(expected = DateTimeParseException.class)
    public void shouldThrowDateTimeParseExceptionBecauseNotValidMonth() {
        AssignmentModel wrongMonthExam = new AssignmentModel("Test", "info", 120, 300, "2015-20-20T10:00", "2015-01-20T10:00");
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldBe28DaysInFeb() {
        AssignmentModel twoManyDaysInFebModel = new AssignmentModel("Test", "info", 120, 300, "2015-02-29T10:00", "2015-01-20T10:00");
    }

    @Test
    public void shouldBe29DaysInFebLeapYear() {
        AssignmentModel learYearModel = new AssignmentModel("Test", "info", 120, 300, "2016-02-29T10:00", "2016-03-20T10:00");
        assertEquals(29, learYearModel.getStartDate().getDayOfMonth());
    }

    @Test(expected = DateTimeParseException.class)
    public void hourShouldBeUnder23() {
        AssignmentModel wrongHourModel = new AssignmentModel("Test", "info", 120, 300, "2015-01-20T24:00", "2015-01-20T10:00");
    }

    @Test(expected = DateTimeParseException.class)
    public void minuteShouldBeUnder59() {
        AssignmentModel wrongMinuteModel = new AssignmentModel("Test", "info",  120, 300, "2015-01-20T10:60", "2015-01-20T10:00");
    }

    @Test(expected = InputMismatchException.class)
    public void minTimeShouldBeSmallerThanMaxTime() {
        AssignmentModel minTimeIsLargerThanMaxTimeModel = new AssignmentModel("Test", "info", 300, 120, "2015-01-20T10:00", "2015-01-20T10:00");
    }

    @Test
    public void shouldNotThrowExceptionSinceMaxTimeIs0() {
        assignmentModel.setMaxTimeSeconds(0);
        assignmentModel.setMinTimeSeconds(120);
        assertEquals(120, assignmentModel.getMinTimeSeconds());
    }

    @Test(expected = InputMismatchException.class)
    public void startDateShouldNotBeAfterEndDate() {
        new AssignmentModel("Test", "info", 300, 120, "2015-01-22T10:00", "2015-01-20T10:00");
    }
}
