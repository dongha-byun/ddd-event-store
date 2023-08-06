package hello.eventstore.order.domain;

public class OrderItem {
    private Long productId;
    private int quantity;


    public OrderItem() {
    }

    public OrderItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toCancelEventPayload() {
        return "{" +
                "\"productId\":" + productId + ", " +
                "\"quantity\":" + quantity +
                "}";
    }
}
