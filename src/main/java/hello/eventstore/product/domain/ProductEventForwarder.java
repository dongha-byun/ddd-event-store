package hello.eventstore.product.domain;

import hello.eventstore.event.domain.EventEntity;
import java.util.List;

public interface ProductEventForwarder {

    List<EventEntity> getEvents(int offset, int limit);
}
