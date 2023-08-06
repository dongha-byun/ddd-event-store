package hello.eventstore.order.infra;

import hello.eventstore.order.domain.Order;
import hello.eventstore.order.domain.OrderRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryOrderRepository implements OrderRepository {
    private static final Map<Long, Order> store = new HashMap<>();
    private static Long sequence = 1L;

    public Order save(Order order) {
        order.assignId(sequence++);
        store.put(order.getId(), order);

        return order;
    }

    public Order findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Order> findAll() {
        return store.values().stream().toList();
    }
}
