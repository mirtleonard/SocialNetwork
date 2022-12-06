package social_network.domain.validator;

import social_network.exceptions.ValidationException;
import social_network.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException, ValidationException;
}
