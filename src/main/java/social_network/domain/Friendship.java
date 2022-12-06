package social_network.domain;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Friendship {
    int user1, user2, id;
    LocalDateTime friendsFrom;

    public Friendship(int id, int user1, int user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.friendsFrom = LocalDateTime.now();
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public int getId() {
        return id;
    }

    public void setFriendsFrom(LocalDateTime date) {
        this.friendsFrom = date;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/YYYY");
        return "first user id: " + user1 + ", second user id: " + user2 + ", friends from: " + friendsFrom.format(formatter);
    }

}
