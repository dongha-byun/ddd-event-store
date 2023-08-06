package hello.eventstore.event.domain;

import java.util.List;

public interface EventStore {

    void save(EventEntity event);
    List<EventEntity> findAll(int offset, int limit);
}
