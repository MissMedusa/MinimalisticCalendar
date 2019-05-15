//CHECKSTYLE:OFF
package View;

import Controller.UserDataLoader;
import Controller.UserProfilesController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
/**
 * The main class.
 */
public class Main extends Application {

    /** Logger. */
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    /** UserDataLoader object */
    private UserDataLoader loader = new UserDataLoader();

    /**
     * The Start function.
     * @param primaryStage the {@code Stage} to show.
     * @throws Exception when the needed FXML files aren't found.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation((getClass().getClassLoader().getResource("fxml/login.fxml")));
        Parent root = fxmlLoader.load();

        primaryStage.setOnCloseRequest(event -> {
            logger.info("Clicked exit button.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.setTitle("Save calendar?");
            alert.setContentText("Do you wish to save your calendar before exiting?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yesButton) {
                try {
                    logger.debug("Saving user, then exiting.");

                    UserProfilesController controller = UserProfilesController.getInstance();
                    loader.saveUser(controller.getUser());
                    logger.debug("Saved user, exiting.");
                } catch (Exception e) {
                    logger.error("Exception while saving profile!");
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Save Error");
                    errorAlert.setContentText("Cannot save changes!");
                    errorAlert.showAndWait();
                }
            } else {
                logger.info("Exited without saving user!");
            }

            Platform.exit();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Minimalistic Calendar");
        primaryStage.setResizable(false);
        primaryStage.show();
        } catch (IOException e) {
        logger.error("IOException on start.");
        }
    }


    /** The main function.
     * @param args the arguments.
     */
    public static void main(String[] args) {
        logger.info("App started.");
        launch(args);
    }
}
