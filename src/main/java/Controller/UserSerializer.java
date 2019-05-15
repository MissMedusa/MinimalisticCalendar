package Controller;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility class for serializing {@code User} generic objects. */
public class UserSerializer {

    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(UserSerializer.class);
    /** GSON Builder object. */
    final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization();
    /** JSON serializer and deserializer. */
    final Gson gson = gsonBuilder.setPrettyPrinting().create();


    /**
     * Serializes a user.
     * @param user the user.
     * @return json the JSON containing the serialized {@code User} object
     */
    public String serializeUser(User user){
        String json = gson.toJson(user);
        return json;
    }

    /**
     * Deserializes a user.
     * @param json the JSON containing the serialized {@code User} object.
     * @return the user read from the JSON.
     */
    public User deserializeUser(String json){
        User user = gson.fromJson(json, User.class);
        logger.info("Deserialized user: " + user);
        return user;
    }
}
