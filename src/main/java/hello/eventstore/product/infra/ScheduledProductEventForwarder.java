package hello.eventstore.product.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventOffset;
import hello.eventstore.event.domain.EventOffsetStore;
import hello.eventstore.event.domain.EventType;
import hello.eventstore.product.domain.Product;
import hello.eventstore.product.domain.ProductRepository;
import hello.eventstore.product.event.OrderCanceledEvent;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class ScheduledProductEventForwarder {

    private final static int DEFAULT_EVENT_BUFFER_SIZE = 100;
    private final EntityManager entityManager;
    private final EventOffsetStore eventOffsetStore;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    // scheduled 실행
    @Scheduled(fixedDelay = 5000)
    public void getAndSend() {
        log.info("scheduled method start");
        long start = System.currentTimeMillis();

        // 마지막 offset 조회
        EventOffset eventOffset = eventOffsetStore.findByEventType(EventType.ORDER_CANCELED);
        if(eventOffset == null) {
            eventOffset = new EventOffset(EventType.ORDER_CANCELED);
            eventOffsetStore.save(eventOffset);
        }
        int offset = eventOffset.getOffset();

        // 이벤트 목록 조회
        List<EventEntity> events = getEvents(offset);

        // 이벤트 전달
        // 이벤트 타입에 맞게 이벤트 객체로 전달
        int successCount = send(events);

        // offset 변경
        eventOffset.updateOffset(offset + successCount);

        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("scheduled method end ===> {}ms", time);
    }

    // 조회한 offset 만큼 이벤트 목록 조회
    public List<EventEntity> getEvents(int offset) {
        return entityManager
                .createQuery(
                        "select e from EventEntity e "
                                + "where 1=1 "
                                + "and eventType= :eventType ", EventEntity.class)
                .setParameter("eventType", EventType.ORDER_CANCELED)
                .setFirstResult(offset)
                .setMaxResults(DEFAULT_EVENT_BUFFER_SIZE)
                .getResultList();
    }

    // 이벤트 전달
    public int send(List<EventEntity> events) {
        int successCount = 0;
        for (EventEntity event : events) {
            try {
                String payload = event.getPayload();
                OrderCanceledEvent orderCanceledEvent = objectMapper.readValue(payload, OrderCanceledEvent.class);

                doProcess(orderCanceledEvent.getProductId(), orderCanceledEvent.getQuantity());
                successCount++;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return successCount;
    }

    private void doProcess(Long productId, int quantity) {
        Product product = productRepository.findById(productId);
        product.decreaseQuantity(quantity);
    }
}
