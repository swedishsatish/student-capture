package studentcapture.helloworld.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;

/**
 * Created by root on 4/25/16.
 */
public class AssignmentModel {

    private static HashMap<String, Integer> validMonths;
    static {
        validMonths = new HashMap<String, Integer>();
        validMonths.put("jan", Calendar.JANUARY);
        validMonths.put("feb", Calendar.FEBRUARY);
        validMonths.put("mar", Calendar.MARCH);
        validMonths.put("apr", Calendar.APRIL);
        validMonths.put("may", Calendar.MAY);
        validMonths.put("jun", Calendar.JUNE);
        validMonths.put("jul", Calendar.JULY);
        validMonths.put("aug", Calendar.AUGUST);
        validMonths.put("sep", Calendar.SEPTEMBER);
        validMonths.put("oct", Calendar.OCTOBER);
        validMonths.put("nov", Calendar.NOVEMBER);
        validMonths.put("dec", Calendar.DECEMBER);
    }

    private String title;
    private int year;
    private String month;
    private int  day;
    private int hour;
    private int minute;
    private int minTimeSeconds;
    private int maxTimeSeconds;

    public AssignmentModel(String title, int year, String month, int day, int hour, int minute, int minTimeSeconds,
                           int maxTimeSeconds) throws InputMismatchException
    {
        this.title = title;
        this.year = year;
        this.month = validateMonth(month);
        this.day = validateDay(year, month, day);
        this.hour = validateHour(hour);
        this.minute = validateMinute(minute);
        this.minTimeSeconds = minTimeSeconds;
        this.maxTimeSeconds = maxTimeSeconds;

        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public AssignmentModel() {
        this.title = "Defualt Assignment";
        this.year = 2000;
        this.month = "jan";
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.minTimeSeconds = 0;
        this.maxTimeSeconds = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) throws InputMismatchException {
        this.month = validateMonth(month);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) throws InputMismatchException {
        this.day = validateDay(year, month, day);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) throws InputMismatchException {
        this.hour = validateHour(hour);
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) throws InputMismatchException {
        this.minute = validateMinute(minute);
    }

    public int getMinTimeSeconds() {
        return minTimeSeconds;
    }

    public void setMinTimeSeconds(int minTimeSeconds) throws  InputMismatchException {
        this.minTimeSeconds = minTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public int getMaxTimeSeconds() {
        return maxTimeSeconds;
    }

    public void setMaxTimeSeconds(int maxTimeSeconds) throws  InputMismatchException {
        this.maxTimeSeconds = maxTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    private String validateMonth(String month) throws InputMismatchException{
        boolean valid = validMonths.containsKey(month);

        if (!valid) {
            throw new InputMismatchException(month + "is not a valid month!");
        }

        return month;
    }

    private int validateDay(int year, String month, int day) throws InputMismatchException{
        Calendar calendar = Calendar.getInstance();
        int maxDay;

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, validMonths.get(month));
        maxDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);

        if (day > maxDay) {
            throw new InputMismatchException("There is only " + maxDay + "in " + month + ", input was " + day);
        } else if (day < 0) {
            throw new InputMismatchException("There can't be a negative amount of days in a month!");
        }

        return day;
    }

    private int validateHour(int hour) throws InputMismatchException {
        if (hour > 23) {
            throw new InputMismatchException("Max hour on a day is 23, input was " + hour);
        } else if (hour < 0) {
            throw new InputMismatchException("Min hour on a day is 0, input was " + hour);
        }

        return hour;
    }

    private int validateMinute(int minute) throws InputMismatchException {
        if (minute > 59) {
            throw new InputMismatchException("Max minute in a hour is 59, input was " + minute);
        } else if (minute < 0) {
            throw new InputMismatchException("Min minute in a hour is 0, input was " + minute);
        }

        return minute;
    }

    private void validateMinMaxTimeSeconds(int minTimeSeconds, int maxTimeSeconds) {
        if ((minTimeSeconds > maxTimeSeconds) && (maxTimeSeconds != 0)) {
            throw new InputMismatchException("Minimum time can't be larger than max time, input was, min: " +
                    minTimeSeconds + " max: " + maxTimeSeconds);
        }
    }
}
