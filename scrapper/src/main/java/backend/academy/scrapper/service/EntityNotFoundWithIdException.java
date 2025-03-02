package backend.academy.scrapper.service;

import backend.academy.scrapper.model.entity.InMemoryStoredEntity;
import lombok.Getter;

@Getter
public class EntityNotFoundWithIdException extends EntityNotFoundException {

    private final Class<? extends InMemoryStoredEntity> entityClass;
    private final Long id;

    public <E extends InMemoryStoredEntity> EntityNotFoundWithIdException(Class<E> entityClass, Long id) {
        super(String.format("%s not found with ID: %s", entityClass.getSimpleName(), id));
        this.entityClass = entityClass;
        this.id = id;
    }
}
