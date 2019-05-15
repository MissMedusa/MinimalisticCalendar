package Controller;

import Model.Event;
import Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Class for managing the user profile.
 */
public class UserProfilesController {
    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(UserProfilesController.class);

    /** The user object. */
    private User user;

    /** UserDataLoader object. */
    private UserDataLoader loader = new UserDataLoader();

    /**
     * An instance of UserProfilesController.
     */
    static UserProfilesController instance;

    /**
     * Gets an instance of UserProfilesController if there is none.
     * @return instance of UserProfilesController
     */
    public static UserProfilesController getInstance() {
        if (instance == null) {
            instance = new UserProfilesController();
        }
        return instance;
    }

    /**
     * Empty Constructor for UserProfilesController class.
     */
    private UserProfilesController() {
        logger.debug("Reading user!");
        this.user = loader.readUser();
        logger.debug("Received user:" + this.user);
    }

    /**
     * Getter method for User.
     * @return user of User class.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Setter method for {@code user}.
     * @param user the user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Adds event to Calendar
     * @param event the event
     */
    public void addEventToCalendar(Event event){
        logger.info("Adding " + event.getName());
        if (user == null) {
            logger.error("No user to add event to !");
        } else {
            getUser().getCalendar().addEvent(event);
        }
    }

    /**
     * Gets the events of the user.
     * @return events the events.
     */
    public ArrayList<Event> getEvents() {
        if (user == null) {
            logger.error("No user to get events of!");
            return new ArrayList<>();
        } else {
            logger.info("Getting users events " + this.getUser().getCalendar().getEvents());
            return getUser().getCalendar().getEvents();
        }
    }


    /**
     * Matches the username and the password.
     * @param username
     * @param encryptedPassword
     * @return
     */
    public boolean userMatches(String username, String encryptedPassword) {
        logger.info("User matches: " + getUser());
        logger.info("Username: " + username);
        logger.info("user-Username: " + getUser().getUsername());
        logger.info("Pass: " + encryptedPassword);
        if (!getUser().getUsername().equals(username)) {
            logger.debug("Username did not match!" );
            return false;
        }
        if (getUser().getEncryptedPassword().equals(encryptedPassword)) {
            return true;
        }

        logger.debug("Password did not match!" );
        return false;
    }
}
