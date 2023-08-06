package hello.eventstore.order.presentation;

import hello.eventstore.order.application.OrderService;
import hello.eventstore.order.domain.Order;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/event")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> save(@RequestBody Order order) {
        Order savedOrder = orderService.save(order);
        return ResponseEntity.created(URI.create("/orders"+savedOrder.getId())).body(savedOrder);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable("id") Long id) {
        Order order = orderService.cancel(id);
        return ResponseEntity.ok(order);
    }
}
