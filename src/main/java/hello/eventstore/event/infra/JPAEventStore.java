package hello.eventstore.event.infra;

import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventStore;
import hello.eventstore.event.domain.EventType;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class JPAEventStore implements EventStore {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void save(EventEntity event) {
        entityManager.persist(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventEntity> findAll(int offset, int limit) {
        return entityManager.createQuery(
                        "select e from EventEntity e ", EventEntity.class
                )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
