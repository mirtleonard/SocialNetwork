package social_network.domain.validator;

import social_network.domain.User;
import social_network.exceptions.ValidationException;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    public void validate(User user) throws ValidationException {
        if (Objects.equals(user.getName(), "")) {
            throw new ValidationException("Username can not be empty.\n");
        }
    }
}
