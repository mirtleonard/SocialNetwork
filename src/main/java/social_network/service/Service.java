package social_network.service;

import social_network.domain.Friendship;
import social_network.domain.User;
import social_network.domain.validator.FriendshipValidator;
import social_network.domain.validator.UserValidator;
import social_network.exceptions.RepositoryException;
import social_network.exceptions.ValidationException;
import social_network.repository.FriendshipRepository;
import social_network.repository.UserRepository;
import social_network.repository.database.FriendshipDBRepository;
import social_network.repository.database.UserDBRepository;

import java.util.*;
import static java.util.stream.Collectors.toCollection;

public class Service {
    private UserRepository userRepo;
    private FriendshipRepository friendshipRepo;

    private UserValidator userValidator;
    private FriendshipValidator friendshipValidator;
    private User currentUser;

    public Service() {
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        this.friendshipRepo = new FriendshipDBRepository(url, "postgres", "postgres");
        this.userRepo = new UserDBRepository(url, "postgres", "postgres");
        this.userValidator = new UserValidator();
        this.friendshipValidator = new FriendshipValidator();
        updateUserFriends();
    }
    private void updateUserFriends() {
        for (Friendship friendship : friendshipRepo.getAll().values()) {
            System.out.println(friendship);
            User user1, user2;
            try {
                user1 = userRepo.find(friendship.getUser1());
                user2 = userRepo.find(friendship.getUser2());
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
                user1.addFriend(user2.getId());
                user2.addFriend(user1.getId());
        }
    }

    private void markCommunity(User user,  Set<Integer> seen) throws RepositoryException {
        seen.add(user.getId());
        for (Integer friendId : user.getFriends()) {
            if (!seen.contains(friendId)) {
                User friend = userRepo.find(friendId);
                markCommunity(friend, seen);
            }
        }
    }

    public Integer communitiesNumber() throws RepositoryException {
        Integer number = 0;
        Map<Integer, User> users = userRepo.getAll();
        Set<Integer> seen = new HashSet<Integer>();
        for (User user : users.values()) {
            if (!seen.contains(user.getId())) {
                ++number;
                markCommunity(user, seen);
            }
        }
        return number;
    }

    public Integer findLongestPath(User user, Set<Integer> seen, Integer path, List<User> community) throws RepositoryException{
        seen.add(user.getId());
        community.add(user);
        Integer maximalPath = path;
        for (Integer friendId : user.getFriends()) {
            if (!seen.contains(friendId)) {
                maximalPath = Math.max(maximalPath, findLongestPath(userRepo.find(friendId), seen, path + 1, community));
            }
        }
        return maximalPath;
    }

    public void mostSocialCommunity() throws RepositoryException {
        Integer longestPath = -1;
        List<User> community, bestCommunity = new ArrayList<>();
        Map<Integer, User> users = userRepo.getAll();
        for (User user : users.values()) {
            community = new ArrayList<User>();
            Integer path = findLongestPath(user, new HashSet<Integer>(), 0, community);
            if (path > longestPath) {
                longestPath = path;
                bestCommunity = community;
            }
        }
        for (User user : bestCommunity) {
            System.out.println(user);
        }
    }

    public void addUser(String name, String email, String password) throws ValidationException {
        User user = new User(userRepo.getLastId(), name, email, password);
        userValidator.validate(user);
        userRepo.add(user);
    }

    public void removeUser(Integer id) throws RepositoryException {
        try {
            User user = userRepo.find(id);
            Set<Integer> set = user.getFriends();
            Integer[] friends = new Integer[set.size()];
            int k = 0;
            for (Integer friend : set) {
                friends[k++] = friend;
            }

            for (Integer friendId : friends) {
                removeFriendship(id, friendId);
            }
            userRepo.remove(id);
        } catch(NullPointerException e) {
            throw new RepositoryException("Id doesn't exist!\n");
        }
    }
    public void addFriendship(int firstId, int secondId) throws RepositoryException, ValidationException {
        try {
            User firstUser = userRepo.find(firstId);
            if (firstUser.hasFriend(secondId)) {
                throw new RepositoryException("Request already sent!\n");
            }
            User secondUser = userRepo.find(secondId);
            Friendship friendship = new Friendship(friendshipRepo.getLastId(), firstId, secondId);
            friendshipValidator.validate(friendship);
            friendshipRepo.add(friendship);
            firstUser.addFriend(secondId);
            secondUser.addFriend(firstId);
        } catch (RuntimeException e) {
            throw new RepositoryException("Id doesn't exist!\n");
        }
    }

    /*
    public void addFriendship(int firstId, int secondId) throws RepositoryException, ValidationException {
        try {
            User firstUser = userRepo.find(firstId);
            Friendship friendship = friendshipRepo.findByUsers(firstId, secondId);
            if (friendship.getUser1() == firstId) {
                throw new RepositoryException("Friend request already sent!\n");
            }
            if (friendshipRepo.findByUsers()) {
            }
            User secondUser = userRepo.find(secondId);
            Friendship friendship = new Friendship(friendshipRepo.getLastId(), firstId, secondId);
            friendshipValidator.validate(friendship);
            friendshipRepo.add(friendship);
            firstUser.addFriend(secondId);
            secondUser.addFriend(firstId);
        } catch (RuntimeException e) {
            throw new RepositoryException("Id doesn't exist!\n");
        }
    }
    */

    public void removeFriendship(int firstId, int secondId) throws RepositoryException {
        try {
            Friendship friendship =  friendshipRepo.findByUsers(firstId, secondId);
            User firstUser = userRepo.find(firstId);
            System.out.println(firstUser);
            if (!firstUser.hasFriend(secondId)) {
                throw new RepositoryException("Friendship doesn't exist!\n");
            }
            User secondUser = userRepo.find(secondId);
            System.out.println(secondUser);
            firstUser.removeFriend(secondId);
            secondUser.removeFriend(firstId);
            friendshipRepo.remove(friendship.getId());
        } catch (NullPointerException e) {
            throw new RepositoryException("Id doesn't exist!\n");
        }
    }

    public User getUser(Integer id) throws RepositoryException {
        return userRepo.find(id);
    }

    public void showUsers() {
        HashMap<Integer, User> users = userRepo.getAll();
        users.forEach((id, user) -> {
            System.out.println(user);
        });
    }

    public String getFriendshipStatus(int firstId, int secondId) {
        try {
            Friendship friendship = friendshipRepo.findByUsers(firstId, secondId);
            return friendship.getStatus();
        } catch(RepositoryException e) {
            return "Not Friends";
        }
    }

    public Friendship getFriendship(int firstID, int secondID) throws RepositoryException {
        return friendshipRepo.findByUsers(firstID, secondID);
    }
    public List<Friendship> getFriendships() {
        return friendshipRepo.getAll().values().stream().collect(toCollection(ArrayList::new));
    }

    public User findUserByEmail(String text) {
        return userRepo.findByEmail(text);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public User getCurrentUser() {
        return this.currentUser;
    }

    public List<User> getUsers() {
        return userRepo.getAll().values().stream().collect(toCollection(ArrayList::new));
    }

    public void acceptFriendship(Integer id1, Integer id2) throws RepositoryException {
        Friendship friendship = friendshipRepo.findByUsers(id1, id2);
        friendship.setStatus("friendship");
        friendshipRepo.update(friendship);
    }

    public void reload() {
        friendshipRepo.reload();
        userRepo.reload();
        updateUserFriends();
    }
}
