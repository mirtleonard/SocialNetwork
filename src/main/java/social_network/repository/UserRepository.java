package social_network.repository;

import social_network.domain.User;
import social_network.exceptions.RepositoryException;

import java.util.HashMap;

public class UserRepository implements  Repository<Integer, User>{
    protected Integer lastId;
    protected HashMap<Integer, User> users;

    public UserRepository() {
        this.lastId = 0;
        this.users = new HashMap<Integer, User>();
    }

    public void add(User user) {
        users.put(user.getId(), user);
    }

    public Integer getLastId() {
        return lastId++;
    }

    public void remove(Integer id) throws RepositoryException {
        try {
            users.remove(id);
        } catch(NullPointerException E) {
            throw new RepositoryException("Id doesn't exist!");
        }
    }

    public HashMap<Integer, User> getAll() {
        return users;
    }

    public User find(Integer friendId) throws RepositoryException {
       User user = users.get(friendId);
       if (user == null) {
           throw new RepositoryException("Id doesn't exist!");
       }
       return user;
    }

    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void reload() {
       users.clear();
    }
}
