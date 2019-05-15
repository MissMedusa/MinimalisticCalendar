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

    /**  */
    private UserProfilesController profileController;


    /**
     * Empty constructor for CalendarController.
     */
    public CalendarController() {
        logger.info("Creating Calendar Controller");
        profileController = UserProfilesController.getInstance();
        logger.info("ProfileController get ");
    }

    public List<Event> getEventsThisMonth(YearMonth yearMonth){
        List<Event> baseEvents = getEvents().stream()
                .filter(event -> isEventThisMonth(event, yearMonth))
                .collect(Collectors.toList());

        logger.info("Events this month" + baseEvents.size());

        List<Event> eventsWithOccurences = getEvents().stream()
                .filter(event -> hasOccurrenceThisMonth(event, yearMonth))
                .collect(Collectors.toList());

        logger.info("Events with occurences this month " + eventsWithOccurences.size());

        List<Event> eventOccurences = new ArrayList<>();
        eventsWithOccurences.forEach(event -> eventOccurences.addAll(getOccurrences(event, yearMonth)));


        logger.info("Returning events:" + baseEvents.size());
        baseEvents.addAll(eventOccurences);
        logger.info("eventOccurences" + eventOccurences);
        logger.info("Merged events:" + baseEvents.size());
                                                            
        return baseEvents;
    }


    public ArrayList<Event> getEvents() {
        return profileController.getEvents();
    }

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
        if (repetitions.contains(Repetition.ANNUALLY) && event.getDate().getMonth() == yearMonth.getMonth() &&
                (event.getDate().getDayOfMonth() <= getLengthOfMonth(yearMonth))) {
            return true;
        }
        return false;
    }

    public List<Event> getOccurrences(Event event, YearMonth yearMonth) {
        List<Repetition> repetitions = event.getRepetitions();
        if (repetitions.contains(Repetition.DAILY)) {
            return getDailyOccurrences(event, yearMonth);
        }
        if (repetitions.contains(Repetition.WEEKLY)) {
            return getWeeklyOccurrences(event, yearMonth);
        }
        return new ArrayList<>();
    }

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

    public List<Event> getWeeklyOccurrences(Event event, YearMonth yearMonth) {
        logger.info("Getting weekly occurrences for " + event);

        List<Event> weeklyOccurrences = new ArrayList<>();
        LocalDate eventDate = event.getDate();
        //int year = event.getDate().getYear();
        //Month month = event.getDate().getMonth();

        for(int dayIndex = 1; dayIndex <= getLengthOfMonth(yearMonth); dayIndex++ ){
            if (!(YearMonth.from(eventDate).equals(yearMonth) && eventDate.getDayOfMonth() == dayIndex) &&
                 eventDate.getDayOfWeek() == LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayIndex).getDayOfWeek()) {
                weeklyOccurrences.add( createEventOccurrenceWithDate(dayIndex, event) );
            }
        }

        logger.debug("Returning weekly occurrences:" + weeklyOccurrences.size());
        return weeklyOccurrences;
    }

    private Event createEventOccurrenceWithDate(int dayOfMonth, Event event) {
        return new Event(event.getName(),
                LocalDate.of(event.getDate().getYear(), event.getDate().getMonth(), dayOfMonth),
                event.getDescription(),
                new ArrayList<>());
    }

    public boolean isEventThisMonth(Event event, YearMonth yearMonth) {
        return YearMonth.from(event.getDate()).equals(yearMonth);
    }

    public Event createEvent(String name, LocalDate date, ArrayList<Repetition> repetitions, String description) {
        Event event = new Event(name, date, description, repetitions);

        profileController.addEventToCalendar(event);
        return event;
    }

    private void filterEventsByName(List<Event> events, String search){
        //TODO
    }

    private void getEventsByDay(){
        //TODO
    }
}
