package social_network.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import social_network.domain.Friendship;
import social_network.domain.User;
import social_network.exceptions.RepositoryException;
import social_network.exceptions.ValidationException;
import social_network.service.Service;

public class UserDetailsController {
    Service service;
    User user;

    @FXML
    Label nameField, emailField, statusField;
    @FXML
    Button button1, button0;

    UserDetailsController(Service service, User user) {
        this.service = service;
        this.user = user;
    }

    public void update() {
        button1.setVisible(false);
        nameField.setText("Name: " + user.getName());
        emailField.setText("Email: " + user.getEmail());
        Friendship friendship;
        try {
            friendship = service.getFriendship(service.getCurrentUser().getId(), user.getId());
        } catch (RepositoryException e) {
            statusField.setText("Status: Not friends");
            button0.setText("Add friend");
             button0.setOnMouseClicked(event -> {
                addFriendship();
            });
            return;
        }
        System.out.println(friendship.getStatus() == "request");
        if (friendship.getStatus().contains("friendship")) {
            statusField.setText("Status: Friends");
            button0.setText("Remove friend");
            button0.setOnMouseClicked(event -> {
                removeFriendship();
            });
        } else if (friendship.getStatus().contains("request")) {
            if (friendship.getUser1() == service.getCurrentUser().getId()) {
                statusField.setText("Status: Pending");
                button0.setText("Cancel request");
                button0.setOnMouseClicked(event -> {
                    removeFriendship();
                });
            } else {
                button1.setVisible(true);
                statusField.setText("Status: Requested");
                button0.setText("Accept request");
                button0.setOnMouseClicked(event -> {
                    acceptFriendship();
                });
                button1.setText("Decline request");
                button1.setOnMouseClicked(event -> {
                    removeFriendship();
                });
            }
        }
    }

    void addFriendship() {
        try {
            service.addFriendship(service.getCurrentUser().getId(), user.getId());
        } catch (RepositoryException | ValidationException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) button0.getScene().getWindow();
        stage.close();
    }

    void removeFriendship() {
        try {
            service.removeFriendship(service.getCurrentUser().getId(), user.getId());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) button0.getScene().getWindow();
        stage.close();
    }

    void acceptFriendship() {
        try {
            service.acceptFriendship(user.getId(), service.getCurrentUser().getId());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) button0.getScene().getWindow();
        stage.close();
    }
}