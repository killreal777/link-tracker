package backend.academy.scrapper.repository;

import backend.academy.scrapper.model.entity.InMemoryStoredEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryRepository<E extends InMemoryStoredEntity> {

    protected static final long INITIAL_ID_SEQUENCE_VALUE = 1L;

    protected final Map<Long, E> data;
    protected AtomicLong idSequence;

    public InMemoryRepository() {
        this.data = new ConcurrentHashMap<>();
        this.idSequence = new AtomicLong(INITIAL_ID_SEQUENCE_VALUE);
    }

    public List<E> findAll() {
        return new ArrayList<>(data.values());
    }

    public Optional<E> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    public E save(E entity) {
        if (entity.id() == null) {
            entity.id(idSequence.getAndIncrement());
        } else {
            idSequence.compareAndSet(entity.id(), entity.id() + 1);
        }
        data.put(entity.id(), entity);
        return entity;
    }

    public void deleteById(Long id) {
        data.remove(id);
    }
}
