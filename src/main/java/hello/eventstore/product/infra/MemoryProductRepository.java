package hello.eventstore.product.infra;

import hello.eventstore.product.domain.Product;
import hello.eventstore.product.domain.ProductRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryProductRepository implements ProductRepository {
    private static final Map<Long, Product> store = new HashMap<>();
    private static Long sequence = 1L;

    public Product save(Product product) {
        product.assignId(sequence++);
        store.put(product.getId(), product);
        return product;
    }

    public Product findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Product> findAll() {
        return store.values().stream().toList();
    }
}
