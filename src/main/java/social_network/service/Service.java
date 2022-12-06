package social_network.service;

import social_network.domain.Friendship;
import social_network.domain.User;
import social_network.domain.validator.FriendshipValidator;
import social_network.domain.validator.UserValidator;
import social_network.exceptions.RepositoryException;
import social_network.exceptions.ValidationException;
import social_network.repository.FriendshipRepository;
import social_network.repository.UserRepository;

import java.util.*;

public class Service {
    private UserRepository userRepo;
    private FriendshipRepository friendshipRepo;

    private UserValidator userValidator;
    private FriendshipValidator friendshipValidator;

    public Service(UserRepository userRepo, FriendshipRepository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.userValidator = new UserValidator();
        this.friendshipValidator = new FriendshipValidator();
        for (Friendship friendship : friendshipRepo.getAll().values()) {
            try {
                User user1 = userRepo.find(friendship.getUser1());
                User user2 = userRepo.find(friendship.getUser2());
                user1.addFriend(user2.getId());
                user2.addFriend(user1.getId());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
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

    public void addUser(String name) throws ValidationException {
        User user = new User(userRepo.getLastId(), name);
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
                throw new RepositoryException("Friendship already exists!\n");
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

    public void removeFriendship(int firstId, int secondId) throws RepositoryException {
        try {
            User firstUser = userRepo.find(firstId);
            if (!firstUser.hasFriend(secondId)) {
                throw new RepositoryException("Friendship doesn't exist!\n");
            }
            User secondUser = userRepo.find(secondId);
            firstUser.removeFriend(secondId);
            secondUser.removeFriend(firstId);
            System.out.println(firstUser);
            friendshipRepo.remove(friendshipRepo.getId(firstId, secondId));
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

    public void showFriendships() {
        Collection<Friendship> friendships = friendshipRepo.getAll().values();
        for (Friendship friendship : friendships) {
            System.out.println(friendship);
        }
    }

}
