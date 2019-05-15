package Model;

import java.util.UUID;

/**
 * Represents the users profile.
 */
public class User {

    /** Id of User.*/
    private String id;
    /** Username of User.*/
    private String username;
    /** EncryptedPassword of User.*/
    private String encryptedPassword;
    /** Calendar of User.*/
    private Calendar calendar;

    /**
     * Constructor for the User class.
     * @param username the username.
     * @param encryptedPassword the encryptedPassword.
     */
    public User(String username, String encryptedPassword) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.calendar = new Calendar("DEFAULT");
    }

    /**
     * Constructor for the User class.
     * @param username the username.
     * @param encryptedPassword the encryptedPassword.
     * @param calendar the calendar.
     */
    public User(String username, String encryptedPassword, Calendar calendar) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.calendar = calendar;
    }

    /**
     * Getter method for Id.
     * @return the id of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for username.
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for {@code username}.
     * @param username the type of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for encryptedPassword.
     * @return the encryptedPassword of the user.
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * Setter method for {@code encryptedPassword}.
     * @param encryptedPassword the type of the user.
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * Getter method for calendar.
     * @return the calendar of the user.
     */
    public Calendar getCalendar() {
        return calendar;
    }


    /**
     * The User toString method.
     * @return the object formatted as a string.
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", calendar=" + calendar +
                '}';
    }
}
