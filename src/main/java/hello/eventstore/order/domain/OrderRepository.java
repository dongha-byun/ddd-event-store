package hello.eventstore.order.domain;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);
    Order findById(Long id);
    List<Order> findAll();
}
