package social_network.ui;

import social_network.domain.User;
import social_network.exceptions.RepositoryException;
import social_network.exceptions.ValidationException;
import social_network.service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CLI {
    private Service service;
    private static BufferedReader reader;

    public CLI(Service srv) {
         reader = new BufferedReader(new InputStreamReader(System.in));
         service = srv;
    }

    private void help() {
        System.out.println(
                "add user\n" +
                "remove user\n" +
                "show users\n" +
                "add friendship\n" +
                "show friendships\n" +
                "remove friendship\n" +
                "get communities\n" +
                "most sociable community\n" +
                "exit \n");
    }
    private void addUser() throws IOException, ValidationException {
        System.out.print("Insert username: ");
        String username;
        username = reader.readLine();
        service.addUser(username);
    }

    private void removeUser() throws IOException, NumberFormatException, RepositoryException {
        System.out.print("Insert user id: ");
        String stringId;
        stringId = reader.readLine();
        int id = Integer.parseInt(stringId);
        service.removeUser(id);
    }

    private void showUsers() {
        service.showUsers();
    }

    private void showFriendships() {
        service.showFriendships();
    }
    private void addFriendship() throws IOException, NumberFormatException, RepositoryException, ValidationException {
        System.out.print("Insert the first user id: ");
        int firstId = Integer.parseInt(reader.readLine());
        System.out.print("Insert the second user id: ");
        int secondId = Integer.parseInt(reader.readLine());
        service.addFriendship(firstId, secondId);
    }
    private void removeFriendship() throws IOException, NumberFormatException, RepositoryException {
        System.out.print("Insert the first user id: ");
        int firstId = Integer.parseInt(reader.readLine());
        System.out.print("Insert the second user id: ");
        int secondId = Integer.parseInt(reader.readLine());
        service.removeFriendship(firstId, secondId);
    }
    public void run() {
        while (true) {
            String input;
            System.out.print(">> ");
            try {
                input = reader.readLine();
                switch (input) {
                    case "add user":
                        addUser();
                        break;
                    case "remove user":
                        removeUser();
                        break;
                    case "show users":
                        showUsers();
                        break;
                    case "add friendship":
                        addFriendship();
                        break;
                    case "show friendships":
                        showFriendships();
                        break;
                    case "remove friendship":
                        removeFriendship();
                        break;
                    case "get communities":
                        System.out.println(service.communitiesNumber());
                        break;
                    case "most sociable community":
                        service.mostSocialCommunity();
                        break;
                    case "help":
                        help();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Unknown command, try 'help'");
                }
            } catch(IOException | NumberFormatException | ValidationException e) {
                System.out.println("Incorrect input!");
            } catch(RepositoryException e) {
                System.out.println(e.getMessage()) ;
            }
        }
    }
}
