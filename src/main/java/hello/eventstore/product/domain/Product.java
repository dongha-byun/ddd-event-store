package hello.eventstore.product.domain;

import lombok.Getter;

@Getter
public class Product {
    private Long id;
    private String name;
    private int price;
    private int quantity;

    public Product() {
    }

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void assignId(Long id) {
        this.id = id;
    }

}
