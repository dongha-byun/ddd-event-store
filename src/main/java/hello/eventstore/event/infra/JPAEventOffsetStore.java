package hello.eventstore.event.infra;

import hello.eventstore.event.domain.EventOffset;
import hello.eventstore.event.domain.EventOffsetStore;
import hello.eventstore.event.domain.EventType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JPAEventOffsetStore implements EventOffsetStore {
    private final EntityManager entityManager;

    @Override
    public void save(EventOffset eventOffset) {
        entityManager.persist(eventOffset);
    }

    @Override
    public EventOffset findByEventType(EventType type) {
        return entityManager.createQuery(
                        "select o from EventOffset o "
                                + "where o.eventType = :eventType", EventOffset.class
                ).setParameter("eventType", type)
                .getSingleResult();
    }
}
