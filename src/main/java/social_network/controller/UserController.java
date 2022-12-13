package social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import social_network.domain.User;
import social_network.service.Service;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserController {
    Service service;
    @FXML
    TextField searchedUser;

    @FXML
    ListView<User> userList;

    ObservableList<User> userModel = FXCollections.observableArrayList();
    public UserController(Service service) {
        this.service = service;
    }


    public void initialize() {
        userList.setItems(userModel);

        userList.setCellFactory(userListView -> {
            return new ListCell<>() {
                public void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        String status = service.getFriendshipStatus(service.getCurrentUser().getId(), user.getId());
                        setText(user.getName() + " " + status);
                    }
                }
            };
        });
        userModel.setAll(service.getUsers());
        searchedUser.textProperty().addListener(o -> handleFilter());
        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User user = userList.getSelectionModel().getSelectedItem();
                showDetails(user);
            }
        });
    }

    private void showDetails(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/userDetailsView.fxml"));
        UserDetailsController controller = new UserDetailsController(service, user);
        fxmlLoader.setController(controller);
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(fxmlLoader.load(), 320, 340);
            stage.setTitle("Details");
            stage.setScene(scene);
            controller.update();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleFilter() {
        Predicate<User> predicate = user -> user.getName().startsWith(searchedUser.getText());
        userModel.setAll(service.getUsers().stream().filter(predicate).collect(Collectors.toList()));
    }
}
