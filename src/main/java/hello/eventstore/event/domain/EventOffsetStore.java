package hello.eventstore.event.domain;

public interface EventOffsetStore {
    void save(EventOffset eventOffset);
    EventOffset findByEventType(EventType type);
}
