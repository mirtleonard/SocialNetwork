package social_network.domain;

import java.util.HashSet;

public class User {
    private Integer id;
    private String name;

    private HashSet<Integer> friends;

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashSet<Integer> getFriends() {
        return friends;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.friends = new HashSet<Integer>();
    }
    public boolean hasFriend(Integer id) {
        return friends.contains(id);
    }
    public void addFriend(Integer id) {
        this.friends.add(id);
    }

    public void removeFriend(Integer id) {
        this.friends.remove(id);
    }

    @Override
    public String toString() {
        return getId().toString() + " " + getName() + " " + getFriends();
    }
}
