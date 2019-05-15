package Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class that represents a calendar.
 */
public class Calendar {
    /** The id of the calendar.*/
    @Expose(serialize = true, deserialize = true)
    private String id;

    /** The name of the calendar.*/
    @Expose(serialize = true, deserialize = true)
    private String name;

    /** The events of the calendar.*/
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Event> events = new ArrayList<Event>();

    /**
     * Constructor for the calendar class.
     * @param name the name of the calendar.
     */
    public Calendar(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
    /**
     * Adds event to calendar.
     * @param event event to add.
     */
    public void addEvent(Event event) {
        this.events.add(event);
    }

    /**
     * Removes event from calendar.
     * @param event event to remove.
     */
    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    /**
     * Getter method for id.
     * @return  id of calendar.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for name.
     * @return name of the calendar.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for {@code name}.
     * @param name the name of the calendar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for events.
     * @return events of the calendar.
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }

    /**
     * Setter method for {@code events}.
     * @param events the events of the calendar.
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
