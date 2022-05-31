package ru.example.sword.tradeshop.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.example.sword.delivery.event.DeliveryEvent;
import ru.example.sword.delivery.model.DeliveryState;
import ru.example.sword.tradeshop.model.OrderState;
import ru.example.sword.tradeshop.service.OrderService;
import ru.example.sword.workshop.event.WorkshopEvent;
import ru.example.sword.workshop.model.ProductState;

import java.util.UUID;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class ConsumerConfiguration {

    private final OrderService orderService;

    public ConsumerConfiguration(OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public Consumer<KStream<UUID, DeliveryEvent>> handleDelivery() {
        return input -> input
                .peek((key, value) -> log.info("Incoming Delivery Event {} {}",
                        value.getProduct().getProduct().getOrderId(), value.getProduct().getState()))
                .peek((key, value) -> handleState(value));
    }

    @Bean
    public Consumer<KStream<UUID, WorkshopEvent>> handleWorkshop() {
        return input -> input
                .filter((key, value) -> value.getProduct().getState() == ProductState.BROKEN)
                .peek((key, value) -> log.info("Incoming Delivery Event {} {}",
                        value.getProduct().getOrderId(), value.getProduct().getState()))
                .peek((key, value) -> handleState(value));
    }

    private void handleState(DeliveryEvent event) {
        DeliveryState state = event.getProduct().getState();
        UUID orderId = event.getProduct().getProduct().getOrderId();
        OrderState newOrderState = switch (state) {
            case COMPLETE -> OrderState.COMPLETED;
            case LOST -> OrderState.CANCELED;
            default -> throw new RuntimeException("Wrong state " + orderId);
        };
        orderService.changeState(orderId, newOrderState);
    }

    private void handleState(WorkshopEvent event) {
        ProductState state = event.getProduct().getState();
        UUID orderId = event.getProduct().getOrderId();
        if (state != ProductState.BROKEN) {
            throw new RuntimeException("Wrong state " + orderId);
        }
        orderService.changeState(orderId, OrderState.CANCELED);
    }
}
