package hello.eventstore.event.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventOffset;
import hello.eventstore.event.domain.EventOffsetStore;
import hello.eventstore.event.domain.EventStore;
import hello.eventstore.event.domain.EventType;
import hello.eventstore.product.domain.Product;
import hello.eventstore.product.domain.ProductEventForwarder;
import hello.eventstore.product.domain.ProductRepository;
import hello.eventstore.product.event.OrderCanceledEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class EventStoreProductEventForwarder implements ProductEventForwarder {
    private final static int DEFAULT_EVENT_BUFFER_SIZE = 100;

    private final EventStore eventStore;
    private final EventOffsetStore eventOffsetStore;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    @Override
    @Scheduled(fixedDelay = 5000)
    public void getAndDoProcess() {
        log.info("scheduled method start");
        long start = System.currentTimeMillis();

        EventOffset eventOffset = getLastOffset(EventType.ORDER_CANCELED);
        int lastOffset = eventOffset.getLastOffset();

        List<OrderCanceledEvent> events = getEvents(lastOffset, DEFAULT_EVENT_BUFFER_SIZE);

        // 이벤트 전달
        // 이벤트 타입에 맞게 이벤트 객체로 전달
        int successCount = send(events);

        eventOffsetStore.updateLastOffset(EventType.ORDER_CANCELED, lastOffset + successCount);

        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("scheduled method end ===> {}ms", time);
    }

    private EventOffset getLastOffset(EventType eventType) {
        EventOffset eventOffset = new EventOffset(eventType);
        try{
            eventOffset = eventOffsetStore.findByEventType(eventType);
        } catch(EmptyResultDataAccessException e) {
            eventOffsetStore.save(eventOffset);
        }

        return eventOffset;
    }

    private List<OrderCanceledEvent> getEvents(int offset, int limit) {
        List<EventEntity> events = eventStore.findAll(offset, limit);
        return events.stream()
                .map(this::getPayloadEvent)
                .toList();
    }

    private OrderCanceledEvent getPayloadEvent(EventEntity event) {
        try {
            String payload = event.getPayload();
            return objectMapper.readValue(payload, OrderCanceledEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private int send(List<OrderCanceledEvent> events) {
        int successCount = 0;
        for (OrderCanceledEvent event : events) {
            doProcess(event);
            successCount++;
        }
        return successCount;
    }

    @Override
    public void doProcess(OrderCanceledEvent event) {
        Product product = productRepository.findById(event.getProductId());
        product.increaseQuantity(event.getQuantity());
    }
}
