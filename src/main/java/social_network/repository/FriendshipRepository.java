package social_network.repository;

import social_network.domain.Friendship;
import social_network.exceptions.RepositoryException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FriendshipRepository implements Repository<Integer, Friendship> {
    protected Integer lastId;
    protected HashMap<Integer, Friendship> friendships;
    public FriendshipRepository() {
        this.lastId = 0;
        friendships = new HashMap<Integer, Friendship>();
    }

    public void add(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
    }

    public Integer getLastId() { return lastId++;}
    public void remove(Integer id) {
        friendships.remove(id);
    }

    public HashMap<Integer, Friendship> getAll() {
        return friendships;
    }

    public Friendship find(Integer id) {
        return friendships.get(id);
    }

    public Friendship findByUsers(Integer firstId, Integer secondId) throws RepositoryException {
        for (Friendship friendship : friendships.values()) {
            if ((friendship.getUser1() == firstId && friendship.getUser2() == secondId) ||
                    (friendship.getUser1() == secondId && friendship.getUser2() == firstId)) {
                return friendship;
            }
        }
        throw new RepositoryException("Friendship not found");
    }

    public void update(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
    }

    public void reload() {
        friendships.clear();
    }
}
