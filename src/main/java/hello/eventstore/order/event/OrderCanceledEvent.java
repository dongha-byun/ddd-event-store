package hello.eventstore.order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCanceledEvent {
    private Long productId;
    private int quantity;
}
