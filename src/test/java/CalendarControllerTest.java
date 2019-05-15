import Controller.CalendarController;
import Controller.UserProfilesController;
import Model.Event;
import Model.Repetition;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import static Model.Repetition.MONTHLY;
import static Model.Repetition.WEEKLY;

public class CalendarControllerTest {

    @Test
    public void getEventsThisMonthTest() {
        CalendarController calendarController = new CalendarController();
        UserProfilesController profileController = UserProfilesController.getInstance();
        ArrayList<Repetition> repeat = new ArrayList<>();

        Event event1 = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);
        Event event2 = new Event("School", LocalDate.of(2019,5,11), "Go to school" , repeat);
        profileController.addEventToCalendar(event1);
        profileController.addEventToCalendar(event2);

        Assert.assertEquals("Added events ", 2, calendarController.getEvents().size());
        Assert.assertEquals("Added events ", 2, calendarController.getEventsThisMonth(YearMonth.of(2019, 5)).size());
        Event event3 = new Event("School", LocalDate.of(2019,5,14), "Go to school" , repeat);

        profileController.addEventToCalendar(event3);
        Assert.assertEquals("Added events ", 3, calendarController.getEventsThisMonth(YearMonth.of(2019, 5)).size());

        ArrayList<Repetition> repeating = new ArrayList<>();
        repeating.add(WEEKLY);
        int weekdayRepetitions = 5;

        Event eventRepeating = new Event("Repeating", LocalDate.of(2019,5,10), "something" , repeating);
        profileController.addEventToCalendar(eventRepeating);

        Assert.assertEquals("Added events ", 3 + weekdayRepetitions, calendarController.getEventsThisMonth(YearMonth.of(2019, 5)).size());
    }

    @Test
    public void getAnnualOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019, 5);
        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(Repetition.ANNUALLY);

        Event event = new Event("School", LocalDate.of(2019, 5, 10), "Go to school" , repeat);

        Assert.assertEquals("Annual Occurrences", 0, calendarController.getOccurrences(event, yearMonth).size());
        YearMonth previousYearMonth = YearMonth.of(2020, 5);

        Assert.assertEquals("Annual Occurrences", 1, calendarController.getOccurrences(event, previousYearMonth).size());

    }

    @Test
    public void getDailyOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019, 5);
        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(Repetition.DAILY);

        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);
        int lengthOfMonth = 31;
        Assert.assertEquals("Daily Occurrences", lengthOfMonth - 1, calendarController.getDailyOccurrences(event, yearMonth).size());
    }

    @Test
    public void getWeeklyOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019, 5);
        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(Repetition.WEEKLY);
        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);
        int weekdayRepetitions = 5;
        Assert.assertEquals("Weekly Occurrences", weekdayRepetitions - 1, calendarController.getWeeklyOccurrences(event, yearMonth).size());
    }

    @Test
    public void getMonthlyOccurrencesTest() {
        YearMonth yearMonth = YearMonth.of(2019, 5);
        CalendarController calendarController = new CalendarController();

        ArrayList<Repetition> repeat = new ArrayList<>();
        repeat.add(MONTHLY);
        Event event = new Event("School", LocalDate.of(2019,5,10), "Go to school" , repeat);

        Assert.assertEquals("Monthly Occurrences", 0, calendarController.getOccurrences(event, yearMonth).size());
        YearMonth previousYearMonth = YearMonth.of(2020, 5);
        Assert.assertEquals("Monthly Occurrences", 1, calendarController.getOccurrences(event, previousYearMonth).size());
    }
}
