package social_network.repository.database;

import social_network.domain.Friendship;
import social_network.domain.User;
import social_network.exceptions.RepositoryException;
import social_network.repository.FriendshipRepository;

import java.sql.*;
import java.time.LocalDateTime;

public class FriendshipDBRepository extends FriendshipRepository {
 String url, username, password;
    public FriendshipDBRepository(String url, String username, String password) {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    @Override
    public void add(Friendship friendship) {
        super.add(friendship);
        saveData(friendship);
    }

    public void saveData(Friendship friendship) {
        try (
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Friendships\" (id, user1, user2, friendsfrom) VALUES(?, ?, ?, ?)");
        ) {
            statement.setInt(1, friendship.getId());
            statement.setInt(2, friendship.getUser1());
            statement.setInt(3, friendship.getUser2());
            statement.setString(4, friendship.getFriendsFrom().toString());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Friendships\"");
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int user1 = resultSet.getInt("user1");
                int user2 = resultSet.getInt("user2");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("friendsFrom"));
                Friendship friendship = new Friendship(id, user1, user2);
                friendship.setFriendsFrom(date);
                super.friendships.put(id, friendship);
                super.lastId = Math.max(super.lastId, id);
            }
            ++super.lastId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeData(Integer id) {
        try (
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Friendships\" WHERE id = ?");
        ) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        removeData(id);
    }
}
