package social_network.exceptions;

public class RepositoryException extends Exception {
    public RepositoryException(String message) {
        super("Repo: " + message);
    }
}
