package hello.eventstore.order.domain;

import java.util.List;
import lombok.Getter;

@Getter
public class Order {
    private Long id;
    private List<OrderItem> items;
    private String orderStatus;

    public Order() {
    }

    public Order(List<OrderItem> items, String orderStatus) {
        this.items = items;
        this.orderStatus = orderStatus;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void cancel() {
        this.orderStatus = "CANCEL";
    }

}
