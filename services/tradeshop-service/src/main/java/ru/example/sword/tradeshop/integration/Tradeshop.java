package ru.example.sword.tradeshop.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.tradeshop.model.OrderRequest;

@MessagingGateway
public interface Tradeshop {

    @Gateway(requestChannel = "inputOrder", replyChannel = "publishOrder")
    Order placeOrder(OrderRequest request);
}
