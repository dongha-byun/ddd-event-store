package hello.eventstore.order.domain;

import hello.eventstore.order.event.OrderCanceledEvent;

public interface OrderEventDispatcher {

    void send(OrderCanceledEvent event);
}
