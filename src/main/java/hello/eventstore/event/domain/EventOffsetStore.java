package hello.eventstore.event.domain;

public interface EventOffsetStore {
    void save(EventOffset eventOffset);
    EventOffset findByEventType(EventType type);
    void updateLastOffset(EventType type, int offset);
}
