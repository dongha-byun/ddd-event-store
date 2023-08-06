package hello.eventstore.event.infra;

import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventStore;
import hello.eventstore.product.domain.ProductEventForwarder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventStoreProductEventForwarder implements ProductEventForwarder {

    private final EventStore eventStore;

    @Override
    public List<EventEntity> getEvents(int offset, int limit) {
        return eventStore.findAll(offset, limit);
    }
}
