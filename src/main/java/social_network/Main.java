package social_network;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import social_network.controller.LoginController;
import social_network.exceptions.RepositoryException;
import social_network.repository.*;
import social_network.repository.database.FriendshipDBRepository;
import social_network.repository.database.UserDBRepository;
import social_network.service.Service;

import java.io.IOException;

public class Main extends Application {
    public static String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
    public static UserRepository repo = new UserDBRepository(url, "postgres", "postgres");
    public static FriendshipRepository repo2 = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");

    public static Service service = new Service();

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        LoginController controller = new LoginController(service);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 320, 340);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}