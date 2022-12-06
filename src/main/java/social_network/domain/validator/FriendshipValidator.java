package social_network.domain.validator;

import social_network.domain.Friendship;
import social_network.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getUser1() == entity.getUser2()) {
            throw new ValidationException("A user can not be friend with himself.\n");
        }
        if (entity.getUser1() < 0 || entity.getUser2() < 0) {
            throw new ValidationException("User id can not be negative.\n");
        }
    }
}
