package hello.eventstore.product.domain;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);
    Product findById(Long id);

    List<Product> findAll();
}
