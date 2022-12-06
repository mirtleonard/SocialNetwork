package social_network;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import social_network.repository.*;
import social_network.repository.database.FriendshipDBRepository;
import social_network.repository.database.UserDBRepository;
import social_network.service.Service;
import social_network.ui.CLI;

import java.io.IOException;

public class Main extends Application {
    public static String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
    public static UserRepository repo = new UserDBRepository(url, "postgres", "postgres");
    public static FriendshipRepository repo2 = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");

    public static Service srv = new Service(repo, repo2);

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        CLI cli = new CLI(srv);
        cli.run();
    }
}