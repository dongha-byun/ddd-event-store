package hello.eventstore.event.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventStore;
import hello.eventstore.event.domain.EventType;
import hello.eventstore.order.domain.OrderEventDispatcher;
import hello.eventstore.order.event.OrderCanceledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventStoreOrderEventDispatcher implements OrderEventDispatcher {

    private final EventStore eventStore;
    private final ObjectMapper objectMapper;

    @Override
    public void send(OrderCanceledEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            EventEntity eventEntity = new EventEntity(
                    EventType.ORDER_CANCELED,
                    payload
            );
            eventStore.save(eventEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
