package model;

import org.junit.Test;
import studentcapture.helloworld.model.ExamModel;

import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 4/25/16.
 */
public class ExamModelTest {

    private ExamModel examModel = new ExamModel(2016, "jan", 22, 15, 0, 120, 300);

    @Test
    public void yearShouldBe2016() {
        assertEquals(2016, examModel.year());
    }

    @Test
    public void monthShouldBeJan() {
        assertEquals("jan", examModel.month());
    }

    @Test
    public void dayShouldBe22() {
        assertEquals(22, examModel.day());
    }

    @Test
    public void hourShouldBe15() {
        assertEquals(15, examModel.hour());
    }

    @Test
    public void minuteShouldBe0() {
        assertEquals(0, examModel.minute());
    }

    @Test
    public void minTimeSecondsShouldBe120() {
        assertEquals(120, examModel.minTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe300() {
        assertEquals(300, examModel.maxTimeSeconds());
    }

    @Test(expected = InputMismatchException.class)
    public void shouldThrowInputMismatchExceptionBecauseNotValidMonth() {
        ExamModel wrongMonthExam = new ExamModel(2016, "notAMonth", 22, 15, 0, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void shouldBe28DaysInFeb() {
        ExamModel twoManyDaysInFebModel = new ExamModel(2015, "feb", 30, 15, 0, 120, 300);
    }

    @Test
    public void shouldBe29DaysInFebLeapYear() {
        ExamModel learYearModel = new ExamModel(2016, "feb", 29, 15, 0, 120, 300);
        assertEquals(29, learYearModel.day());
    }

    @Test(expected = InputMismatchException.class)
    public void hourShouldBeBetween00And23() {
        ExamModel wrongHourModel = new ExamModel(2016, "apr", 22, 24, 0, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void minuteShouldBeBetween00And59() {
        ExamModel wrongMinuteModel = new ExamModel(2016, "dec", 10, 20, 60, 120, 300);
    }

    @Test(expected = InputMismatchException.class)
    public void minTimeShouldBeSmallerThanMaxTime() {
        ExamModel minTimeIsLargerThanMaxTimeModel = new ExamModel(2016, "may", 10, 15, 0, 300, 120);
    }
}
