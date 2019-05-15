//CHECKSTYLE:OFF

package View;

import Controller.UserDataLoader;
import Controller.UserProfilesController;
import Model.Calendar;
import Model.Event;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class LoginController {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserDataLoader loader = new UserDataLoader();

    @FXML
    private Label loginLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signinButton;
    @FXML
    private Button quitButton;

    @FXML
    private void logIn(ActionEvent event){
            logger.info("Logging user in!");

            UserProfilesController controller = UserProfilesController.getInstance();
            if (controller.userMatches(usernameField.getText(), passwordField.getText())) {
               logger.info("User matches!");

                Stage stage;
                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader();
                    logger.info("Getting FXML!");

                    loader.setLocation(this.getClass().getClassLoader().getResource("fxml/main-view.fxml"));
                    root = loader.load();
                    logger.info("loaded  FXML");
                    stage = (Stage) signinButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e){
                    logger.error("Exception occured while loading FXML: " + "fxml/main-view.fxml");

                }
            } else {
                loginLabel.setText("Wrong Username or Password!");
                logger.info("User did not match");
            }
    }

    @FXML
    private void quitLogin(){
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();

    }


}
