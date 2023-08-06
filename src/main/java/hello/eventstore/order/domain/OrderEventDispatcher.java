package hello.eventstore.order.domain;

import hello.eventstore.order.event.OrderCanceledEvent;

public interface OrderEventDispatcher {

    void handle(OrderCanceledEvent event);
}
