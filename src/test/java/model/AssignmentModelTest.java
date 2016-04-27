package model;

import org.junit.Test;
import studentcapture.helloworld.model.AssignmentModel;

import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModelTest {

    private AssignmentModel assignmentModel = new AssignmentModel("Test", 2016, "jan", 22, 15, 0, 120, 300);

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
    public void yearShouldBe2016() {
        assertEquals(2016, assignmentModel.getYear());
    }

    @Test
    public void yearShouldBe2015() {
        assignmentModel.setYear(2015);
        assertEquals(2015, assignmentModel.getYear());
    }

    @Test
    public void monthShouldBeJan() {
        assertEquals("jan", assignmentModel.getMonth());
    }

    @Test
    public void monthShouldBeFeb() {
        assignmentModel.setMonth("feb");
        assertEquals("feb", assignmentModel.getMonth());
    }

    @Test
    public void dayShouldBe22() {
        assertEquals(22, assignmentModel.getDay());
    }

    @Test
    public void dayShouldBe23() {
        assignmentModel.setDay(23);
        assertEquals(23, assignmentModel.getDay());
    }

    @Test
    public void hourShouldBe15() {
        assertEquals(15, assignmentModel.getHour());
    }

    @Test
    public void hourShouldBe16() {
        assignmentModel.setHour(16);
        assertEquals(16, assignmentModel.getHour());
    }

    @Test
    public void minuteShouldBe0() {
        assertEquals(0, assignmentModel.getMinute());
    }

    @Test
    public void minuteShouldBe30() {
        assignmentModel.setMinute(30);
        assertEquals(30, assignmentModel.getMinute());
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

    @Test(expected = InputMismatchException.class)
    public void shouldThrowInputMismatchExceptionBecauseNotValidMonth() {
        AssignmentModel wrongMonthExam = new AssignmentModel("Test", 2016, "notAMonth", 22, 15, 0, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void shouldBe28DaysInFeb() {
        AssignmentModel twoManyDaysInFebModel = new AssignmentModel("Test", 2015, "feb", 30, 15, 0, 120, 300);
    }

    @Test
    public void shouldBe29DaysInFebLeapYear() {
        AssignmentModel learYearModel = new AssignmentModel("Test", 2016, "feb", 29, 15, 0, 120, 300);
        assertEquals(29, learYearModel.getDay());
    }

    @Test(expected = InputMismatchException.class)
    public void hourShouldBeUnder23() {
        AssignmentModel wrongHourModel = new AssignmentModel("Test", 2016, "apr", 22, 24, 0, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void hourShouldBeAtleast0() {
        AssignmentModel wrongHourModel = new AssignmentModel("Test", 2016, "mar", 22, -1, 0, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void minuteShouldBeUnder59() {
        AssignmentModel wrongMinuteModel = new AssignmentModel("Test", 2016, "dec", 10, 20, 60, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void minuteShouldBeAtleast0() {
        AssignmentModel wrongMinuteModel = new AssignmentModel("Test", 2016, "nov", 10, 15, -1, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void minTimeShouldBeSmallerThanMaxTime() {
        AssignmentModel minTimeIsLargerThanMaxTimeModel = new AssignmentModel("Test", 2016, "may", 10, 15, 0, 300, 120);
    }

    @Test
    public void shouldNotThrowExceptionSinceMaxTimeIs0() {
        assignmentModel.setMaxTimeSeconds(0);
        assignmentModel.setMinTimeSeconds(120);
        assertEquals(120, assignmentModel.getMinTimeSeconds());
    }
}
