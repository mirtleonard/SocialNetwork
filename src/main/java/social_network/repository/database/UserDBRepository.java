package social_network.repository.database;

import social_network.domain.User;
import social_network.exceptions.RepositoryException;
import social_network.repository.UserRepository;

import java.sql.*;

public class UserDBRepository extends UserRepository {
    String url, username, password;
    public UserDBRepository(String url, String username, String password) {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    @Override
    public void add(User user) {
        super.add(user);
        saveData(user);
    }

    public void saveData(User user) {
        try (
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Users\" (id, name, email, password) VALUES(?, ?, ?, ?)");
        ) {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users\"");
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(id, name, email, password);
                super.users.put(id, user);
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
                PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Users\" WHERE id = ?");
        ) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Integer id) throws RepositoryException {
        super.remove(id);
        removeData(id);
    }

    @Override
    public void reload() {
        super.reload();
        loadData();
    }
}
