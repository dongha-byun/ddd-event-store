package hello.eventstore.order.application;

import hello.eventstore.event.domain.EventEntity;
import hello.eventstore.event.domain.EventType;
import hello.eventstore.order.domain.Order;
import hello.eventstore.order.domain.OrderEventDispatcher;
import hello.eventstore.order.domain.OrderItem;
import hello.eventstore.order.domain.OrderRepository;
import hello.eventstore.order.event.OrderCanceledEvent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventDispatcher orderEventDispatcher;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order cancel(Long id) {
        Order order = findById(id);
        order.cancel();

        order.getItems()
                .forEach(
                        this::sendOrderCancelEvent
                );

        return order;
    }

    private void sendOrderCancelEvent(OrderItem orderItem) {
        orderEventDispatcher.send(
                new OrderCanceledEvent(
                        orderItem.getProductId(),
                        orderItem.getQuantity()
                )
        );
    }
}
