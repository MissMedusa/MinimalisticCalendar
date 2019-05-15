package Controller;

import java.time.LocalDate;
import java.time.YearMonth;


/**
 * DateUtils class for dates.
 */
public class DateUtils {

    /**
     * Method that gets the length of a month.
     * @param date the date.
     * @return the number of days.
     */
    public static int getLengthOfMonth(LocalDate date) {
        return YearMonth.of(date.getYear(), date.getMonth()).lengthOfMonth();
    }

    /**
     * Method that gets the length of a month.
     * @param yearMonth YearMonth type object.
     * @return the number of days.
     */
    public static int getLengthOfMonth(YearMonth yearMonth) {
        return yearMonth.lengthOfMonth();
    }
}
