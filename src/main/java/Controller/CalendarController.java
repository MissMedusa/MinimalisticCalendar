package Controller;

import Model.Event;
import Model.Repetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Controller.DateUtils.getLengthOfMonth;

/**
 * Class for handling the Calendar.
 */
public class CalendarController {
    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(UserProfilesController.class);

    /** UserProfilesController object. */
    private UserProfilesController profileController;


    /**
     * Empty constructor for CalendarController.
     */
    public CalendarController() {
        logger.info("Creating Calendar Controller");
        profileController = UserProfilesController.getInstance();
        logger.info("Created Calendar Controller");
    }

    /**
     * Returns the events that are in the month.
     * @param yearMonth the month.
     * @return baseEvents the events
     */
    public List<Event> getEventsThisMonth(YearMonth yearMonth){
        List<Event> baseEvents = getEvents().stream()
                .filter(event -> isEventThisMonth(event, yearMonth))
                .collect(Collectors.toList());

        logger.info("Events this month: " + baseEvents.size());

        List<Event> eventsWithOccurrences = getEvents().stream()
                .filter(event -> hasOccurrenceThisMonth(event, yearMonth))
                .collect(Collectors.toList());

        logger.info("Events with occurrences this month: " + eventsWithOccurrences.size());

        List<Event> eventOccurrences = new ArrayList<>();
        eventsWithOccurrences.forEach(event -> eventOccurrences.addAll(getOccurrences(event, yearMonth)));


        logger.info("Returning events: " + baseEvents.size());
        baseEvents.addAll(eventOccurrences);
        logger.info("eventOccurrences: " + eventOccurrences);
        logger.info("Merged events:" + baseEvents.size());
                                                            
        return baseEvents;
    }


    /**
     * Getter method for events.
     * @return events.
     */
    public ArrayList<Event> getEvents() {
        return profileController.getEvents();
    }

    /**
     * Checks if event occurred this month.
     * @param event the event.
     * @param yearMonth the month.
     * @return boolean.
     */
    public boolean hasOccurrenceThisMonth(Event event, YearMonth yearMonth) {
        List<Repetition> repetitions = event.getRepetitions();

        if (repetitions.contains(Repetition.DAILY) || repetitions.contains(Repetition.WEEKLY)) {
            return true;
        }

        //todo check april 31
        if (repetitions.contains(Repetition.MONTHLY) && (event.getDate().getDayOfMonth() <= getLengthOfMonth(yearMonth))) {
            return true;
        }

        //todo take note on febr 29:
        return repetitions.contains(Repetition.ANNUALLY) && event.getDate().getMonth() == yearMonth.getMonth() &&
                (event.getDate().getDayOfMonth() <= getLengthOfMonth(yearMonth));
    }

    /**
     * Returns occurrences of the event that month. An occurence is a date that the event occurs in, but does not include the original date itself.
     * @param event the event.
     * @param yearMonth the month.
     * @return list of events.
     */
    public List<Event> getOccurrences(Event event, YearMonth yearMonth) {
        List<Repetition> repetitions = event.getRepetitions();
        if (repetitions.contains(Repetition.DAILY)) {
            return getDailyOccurrences(event, yearMonth);
        }
        if (repetitions.contains(Repetition.WEEKLY)) {
            return getWeeklyOccurrences(event, yearMonth);
        }
        if (repetitions.contains(Repetition.MONTHLY)) {
            return getMonthlyOccurences(event, yearMonth);
        }
        if (repetitions.contains(Repetition.ANNUALLY)) {
            return getAnnualOccurences(event, yearMonth);
        }

        return new ArrayList<>();
    }

    /**
     * Returns the daily occurrences of the event.
     * @param event the event.
     * @param yearMonth the month.
     * @return dailyOccurrences the occurences in @{Code yearMonth} month.
     */
    public List<Event> getDailyOccurrences(Event event, YearMonth yearMonth) {
        List<Event> dailyOccurrences = new ArrayList<>();
        LocalDate eventDate = event.getDate();

        for(int dayIndex = 1; dayIndex <= getLengthOfMonth(yearMonth); dayIndex++ ){
            if (dayIndex != eventDate.getDayOfMonth() ) {
                dailyOccurrences.add( createEventOccurrenceWithDate(dayIndex, event) );
            }
        }

        logger.info("Returning daily occurrences for event " + event + " length: " + dailyOccurrences.size());
        return dailyOccurrences;
    }

    /**
     * Returns the monthly occurrences of the event in the month. Will return the single event if the month is not the same as the original events month.
     * @param event the event.
     * @param yearMonth the month.
     * @return weeklyOccurrences the occurences in @{Code yearMonth} month.
     */
    public List<Event> getMonthlyOccurences(Event event, YearMonth yearMonth) {
        List<Event> monthlyOccurences = new ArrayList<>();
        LocalDate eventDate = event.getDate();

        if (!(YearMonth.from(eventDate).equals(yearMonth)) && getLengthOfMonth(yearMonth) >= eventDate.getDayOfMonth()) {
            monthlyOccurences.add(createEventOccurrenceWithDate( eventDate.getDayOfMonth(), event));
        }
        return monthlyOccurences;
    }

    /**
     * Returns the yearly occurrences of the event in the month.
     * @param event the event.
     * @param yearMonth the month.
     * @return yearlyOccurence the occurences in @{Code yearMonth} month.
     */
    public List<Event> getAnnualOccurences(Event event, YearMonth yearMonth) {
        List<Event> yearlyOccurences = new ArrayList<>();
        LocalDate eventDate = event.getDate();

        logger.info("get annual occurences: " + event + " - " + yearMonth);
        if (!(YearMonth.from(eventDate).equals(yearMonth)) && (yearMonth.getMonth() == eventDate.getMonth())) {
            yearlyOccurences.add(createEventOccurrenceWithDate( eventDate.getDayOfMonth(), event));
        }
        return yearlyOccurences;
    }

    /**
     * Returns the weekly occurrences of the event in the month.
     * @param event the event.
     * @param yearMonth the month.
     * @return weeklyOccurrences.
     */
    public List<Event> getWeeklyOccurrences(Event event, YearMonth yearMonth) {
        logger.info("Getting weekly occurrences for " + event);

        List<Event> weeklyOccurrences = new ArrayList<>();
        LocalDate eventDate = event.getDate();

        for(int dayIndex = 1; dayIndex <= getLengthOfMonth(yearMonth); dayIndex++ ){
            if (!(YearMonth.from(eventDate).equals(yearMonth) && eventDate.getDayOfMonth() == dayIndex) &&
                 eventDate.getDayOfWeek() == LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayIndex).getDayOfWeek()) {
                weeklyOccurrences.add( createEventOccurrenceWithDate(dayIndex, event) );
            }
        }

        logger.debug("Returning weekly occurrences:" + weeklyOccurrences.size());
        return weeklyOccurrences;
    }


    /**
     * Creates event with date.
     * @param dayOfMonth the day of month.
     * @param event the event.
     * @return event.
     */
    private Event createEventOccurrenceWithDate(int dayOfMonth, Event event) {
        return new Event(event.getName(),
                LocalDate.of(event.getDate().getYear(), event.getDate().getMonth(), dayOfMonth),
                event.getDescription(),
                new ArrayList<>());
    }

    /**
     * Checks if the event is in this month.
     * @param event the event.
     * @param yearMonth the month.
     * @return boolean.
     */
    public boolean isEventThisMonth(Event event, YearMonth yearMonth) {
        return YearMonth.from(event.getDate()).equals(yearMonth);
    }

    /**
     * Creates an event and adds it to the calendar.
     * @param name the name.
     * @param date the date.
     * @param repetitions the repetition.
     * @param description the description.
     * @return event.
     */
    public Event createEvent(String name, LocalDate date, ArrayList<Repetition> repetitions, String description) {
        Event event = new Event(name, date, description, repetitions);

        profileController.addEventToCalendar(event);
        return event;
    }

}
