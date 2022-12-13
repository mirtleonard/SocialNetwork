package social_network.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import social_network.domain.Friendship;
import social_network.exceptions.RepositoryException;
import social_network.service.Service;

import java.util.function.Predicate;
import java.util.stream.Collectors;


public class FriendRequestController {
    Service service;
    @FXML
    TableView<Friendship> friendshipTable;
    @FXML
    TableColumn<Friendship, String> userNameColumn;
    @FXML
    TableColumn<Friendship, String> dateColumn;
    @FXML
    TableColumn<Friendship, String> statusColumn;

    ObservableList<Friendship> friendshipModel = FXCollections.observableArrayList();

    public FriendRequestController(Service service) {
        this.service = service;
    }

    public void initialize() {
        userNameColumn.setCellValueFactory(cellData-> {
            try {
                Friendship friendship = cellData.getValue();
                String name;
                name = service.getUser(friendship.getUser1()).getName();
                return new SimpleStringProperty(name);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<Friendship, String>("friendsFrom"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Friendship, String>("status"));

        friendshipTable.setItems(friendshipModel);
        handleFilter();
    }
    private void handleFilter() {
        Predicate<Friendship> p1 = friendship -> friendship.getUser2() == service.getCurrentUser().getId();
        friendshipModel.setAll(
                service.getFriendships()
                        .stream()
                        .filter(p1)
                        .collect(Collectors.toList()));
    }
}
