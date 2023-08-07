package hello.eventstore.product.domain;

import hello.eventstore.product.event.OrderCanceledEvent;

public interface ProductEventForwarder {

    void getAndDoProcess();

    void doProcess(OrderCanceledEvent event);
}
