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

    public Integer getId(Integer id1, Integer id2) {
        for (Friendship friendship : friendships.values()) {
            if (friendship.getUser1() == id1 && friendship.getUser2() == id2) {
                return friendship.getId();
            }
        }
        return -1;
    }
    public void add(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
    }

    public Integer getLastId() { return lastId++;}
    public void remove(Integer id) {
        System.out.println(id);
        friendships.remove(id);
    }

    public HashMap<Integer, Friendship> getAll() {
        return friendships;
    }

    public Friendship find(Integer id) {
        return friendships.get(id);
    }
}
