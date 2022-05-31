package ru.example.sword.tradeshop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.tradeshop.model.OrderRequest;
import ru.example.sword.tradeshop.model.OrderState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ConcurrentMap<UUID, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order(UUID.randomUUID(), orderRequest.getUserId(), OrderState.PROCESSING, orderRequest.getName());
        orders.put(order.getId(), order);
        log.debug("Created new order: {} {} {}", order.getId(), order.getUserId(), order.getName());
        return order;
    }

    @Override
    public List<Order> orderList() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public void changeState(UUID orderId, OrderState orderState) {
        Order order = orders.get(orderId);
        if (order == null) {
            log.info("accounting has lost the order {}", orderId);
            return;
        }
        order.setState(orderState);
        log.debug("Changed order {} state to {}", orderId, orderState);
    }
}
