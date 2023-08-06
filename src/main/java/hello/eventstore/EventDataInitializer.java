package hello.eventstore;

import hello.eventstore.order.domain.Order;
import hello.eventstore.order.domain.OrderItem;
import hello.eventstore.order.domain.OrderRepository;
import hello.eventstore.product.domain.Product;
import hello.eventstore.product.domain.ProductRepository;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDataInitializer {

    private final InitService initService;

    public EventDataInitializer(InitService initService) {
        this.initService = initService;
    }

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    static class InitService {

        @Autowired
        ProductRepository productRepository;

        @Autowired
        OrderRepository orderRepository;

        public void init() {
            Product product1 = productRepository.save(new Product("상품1", 1000, 5));
            Product product2 = productRepository.save(new Product("상품2", 2000, 10));
            Product product3 = productRepository.save(new Product("상품3", 3000, 15));

            List<OrderItem> items = Arrays.asList(
                    new OrderItem(product1.getId(), 2),
                    new OrderItem(product2.getId(), 3)
            );
            Order order = new Order(items, "PREPARED");
            orderRepository.save(order);
        }
    }
}
