package Controller;

import Model.User;
import Model.UserEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for managing the user data.
 */
public class UserDataLoader {

    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(UserDataLoader.class);

    /** Users database. */
    private final String USERS_FILE = "userData/users.json";

    /** UserSerializer object. */
    private UserSerializer userSerializer = new UserSerializer();

    /**
     * Method to save the user.
     * @param user the user.
     */
    public void saveUser(User user) {
        logger.info("Saving users: ", user);
        String jsonToWrite = userSerializer.serializeUser(user);
        File file = new File(USERS_FILE);
        try {
            FileUtils.writeStringToFile(file, jsonToWrite, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            logger.error("IOException while writing user profile to file: ", file.getPath());
            logger.error(e.getMessage());
        }
    }

    /**
     * Reads all users from the database.
     * @return user.
     */
    public User readUser() {
            logger.info("Initializing user.");
            File file = new File(USERS_FILE);
            String usersJson = new String();
            //temporary user in case no other exists
            User user = new User("admin", "admin");
            if(file.exists()){
                logger.info("User profile file exists!");
                try {
                    usersJson = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    user = userSerializer.deserializeUser(usersJson);
                    logger.info("Deserialized users: " + user.toString());
                } catch (IOException e) {
                    logger.error("Error, user file doesn't exist!");
                } catch (Exception e ){
                    logger.error("Error while deserializing profile file!");
                }
            } else {
                logger.info("User file doesn't exist, creating it.");
                saveUser(user);
            }
            return user;
        }


    /**
     * Method that reads JSON from files.
     * @param filePath the file path.
     * @return result in a String.
     */
    private String readJSONfromFile (String filePath){
        logger.info("Reading file: " + filePath);
        String result = "";
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath);
            result = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(filePath),"UTF-8");
        }
        catch(IOException e){
            logger.error("Exception while reading {}", filePath);
            logger.error(e.getMessage());
        }
        return result;
    }
}
