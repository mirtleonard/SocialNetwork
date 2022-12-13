package social_network.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import social_network.service.Service;

import java.io.IOException;

public class MainController {

    Service service;
    @FXML
    SplitPane mainPain;
    @FXML
    AnchorPane displayPane;

    @FXML
    Label userNameLabel;
    public MainController(Service srv) {
        this.service = srv;
    }

    public void setDetails() {
        userNameLabel.setText("Hello " + service.getCurrentUser().getName());
    }

    public void getRequests() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/friendRequestView.fxml"));
        FriendRequestController controller = new FriendRequestController(service);
        loader.setController(controller);

        //Stage stage = new Stage();
        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = (Stage) displayPane.getScene().getWindow();
        ObservableList<Node> displayNodes = displayPane.getChildren();
        displayNodes.clear();
        displayNodes.add(scene.getRoot().getChildrenUnmodifiable().get(0));
        System.out.println(displayNodes);
        //displayPane = scene.getRoot().getChildrenUnmodifiable().get(0);
        //stage.setTitle("Requests");
        //stage.show();
    }
    public void getUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/userView.fxml"));
        UserController controller = new UserController(service);
        loader.setController(controller);
        Scene scene = new Scene(loader.load(), 600, 400);
        ObservableList<Node> displayNodes = displayPane.getChildren();
        displayNodes.clear();
        displayNodes.addAll(scene.getRoot().getChildrenUnmodifiable());
        System.out.println(displayNodes);

        /*
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Users");
        stage.show();
         */
    }

    public void reload() {
        service.reload();
    }
}
