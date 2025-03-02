package backend.academy.scrapper.service;

import java.util.NoSuchElementException;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends NoSuchElementException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
