package social_network.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import social_network.domain.User;
import social_network.service.Service;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField email;
    @FXML
    PasswordField password;

    @FXML
    Label labelEmail;
    Service service;

    public LoginController(Service srv) {
        service = srv;
    }

    @FXML
    protected void login() throws IOException {
        User user;
        user = service.findUserByEmail(email.getText());
        if (user != null && user.getPassword().equals(password.getText())) {
            service.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
            MainController controller = new MainController(service);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 800, 600);

            Stage currentStage = (Stage) email.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Social Network");
            currentStage.close();
            controller.setDetails();
            newStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect email or password", ButtonType.OK);
            alert.show();
        }
    }

}
