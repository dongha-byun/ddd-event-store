package hello.eventstore.event.infra;

import hello.eventstore.event.domain.EventOffset;
import hello.eventstore.event.domain.EventOffsetStore;
import hello.eventstore.event.domain.EventType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class JPAEventOffsetStore implements EventOffsetStore {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void save(EventOffset eventOffset) {
        entityManager.persist(eventOffset);
    }

    @Override
    @Transactional(readOnly = true)
    public EventOffset findByEventType(EventType type) {
        return entityManager.createQuery(
                        "select o from EventOffset o "
                                + "where o.eventType = :eventType", EventOffset.class
                ).setParameter("eventType", type)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void updateLastOffset(EventType type, int offset) {
        EventOffset eventOffset = findByEventType(type);
        eventOffset.updateOffset(offset);
    }
}
