package Model;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that represents an event.
 */
public class Event {
    /**The id of an event. */
    @Expose(serialize = true, deserialize = true)
    private String id;

    /**The name of an event. */
    @Expose(serialize = true, deserialize = true)
    private String name;

    /**The date of an event. */
    @Expose(serialize = true, deserialize = true)
    private LocalDate date;

    /**The description of an event. */
    @Expose(serialize = true, deserialize = true)
    private String description;

    /**The repetitions of an event. */
    @Expose(serialize = true, deserialize = true)
    private List<Repetition> repetitions;

    /**
     * Constructor for Event class.
     * @param name the name of the event.
     * @param date the date of the event.
     * @param description the description of the event.
     * @param repetitions the repetitions of the event.
     */
    public Event(String name, LocalDate date, String description, ArrayList<Repetition> repetitions) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.description = description;
        this.repetitions = repetitions;
    }

    /**
     * Getter method for events.
     * @return the id of the event.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for events.
     * @return the name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for {@code name}.
     * @param name the name of the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for events.
     * @return the date of the event.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter method for {@code date}.
     * @param date the date of the event.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter method for events.
     * @return the description of the event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for {@code description}.
     * @param description the description of the event.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method for events.
     * @return the repetitions of the event.
     */
    public List<Repetition> getRepetitions() {
        return repetitions;
    }

    /**
     * Setter method for {@code repetitions}.
     * @param repetitions the repetitions of the event.
     */
    public void setRepetitions(ArrayList<Repetition> repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * The Event toString method.
     * @return the object formatted as a string.
     */
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
