package Model;

import com.google.gson.annotations.Expose;

/**
 * User object for serializing user objects.
 */
public class UserEntity {
    /** Id of User.*/
    @Expose(serialize = true, deserialize = true)
    private String id;

    /** Username of User.*/
    @Expose(serialize = true, deserialize = true)
    private String username;

    /** EncryptedPassword of User.*/
    @Expose(serialize = true, deserialize = true)
    private String encryptedPassword;

    /** Calendar of User.*/
    @Expose(serialize = true, deserialize = true)
    private Calendar calendar;

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
     * Setter method for {@code calendar}.
     * @param calendar the calendar of the user.
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }


}
