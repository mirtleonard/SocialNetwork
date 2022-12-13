package social_network.domain;

import java.util.HashSet;

public class User {
    private Integer id;
    private String name;
    private String email;

    private String password;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


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

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
        return getId() + " " + getName() + " " + getEmail() + getFriends();
    }
}
