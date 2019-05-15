import Controller.CalendarController;
import Model.Event;
import Model.Repetition;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static Controller.DateUtils.getLengthOfMonth;
import static Model.Repetition.*;

public class CalendarControllerTest {

    @Test
    public void getOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019,05);

        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(ANNUALLY);

        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);


        Assert.assertEquals("Annual Occurrences", 1, calendarController.getOccurrences(event, yearMonth).size());



    }

    @Test
    public void getDailyOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019,05);

        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(DAILY);

        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);


        Assert.assertEquals("Daily Occurrences", 31, calendarController.getDailyOccurrences(event, yearMonth).size());
    }

    @Test
    public void getWeeklyOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019,05);

        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(WEEKLY);

        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);


        Assert.assertEquals("Weekly Occurrences", 5, calendarController.getWeeklyOccurrences(event, yearMonth).size());

    }

}
