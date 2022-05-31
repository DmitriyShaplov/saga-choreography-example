package ru.example.sword.tradeshop.integration;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.example.sword.tradeshop.event.TradeshopEvent;
import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.tradeshop.model.OrderRequest;
import ru.example.sword.tradeshop.service.OrderService;

@Configuration
public class IntegrationConfig {

    private final OrderService orderService;
    private final StreamBridge streamBridge;

    public IntegrationConfig(OrderService orderService, StreamBridge streamBridge) {
        this.orderService = orderService;
        this.streamBridge = streamBridge;
    }

    @ServiceActivator(inputChannel = "inputOrder", outputChannel = "publishOrder")
    public Order handleNewOrder(OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @ServiceActivator(inputChannel = "publishOrder")
    public void publishOrder(Order order) {
        TradeshopEvent tradeshopEvent = new TradeshopEvent(order);
        Message<TradeshopEvent> message = MessageBuilder.withPayload(tradeshopEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, order.getId())
                .build();
        streamBridge.send("order-out-0", message);
    }
}
