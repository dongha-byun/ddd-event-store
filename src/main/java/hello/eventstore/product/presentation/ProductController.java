package hello.eventstore.product.presentation;

import hello.eventstore.product.application.ProductService;
import hello.eventstore.product.domain.Product;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/event")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
}
