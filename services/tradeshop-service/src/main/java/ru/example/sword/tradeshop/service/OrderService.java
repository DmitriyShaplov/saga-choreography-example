package ru.example.sword.tradeshop.service;

import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.tradeshop.model.OrderRequest;
import ru.example.sword.tradeshop.model.OrderState;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(OrderRequest orderRequest);

    List<Order> orderList();

    void changeState(UUID orderId, OrderState orderState);
}
