package ru.example.sword.tradeshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.example.sword.tradeshop.integration.Tradeshop;
import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.tradeshop.model.OrderRequest;
import ru.example.sword.tradeshop.service.OrderService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    private final Tradeshop tradeshop;
    private final OrderService orderService;

    public OrderController(Tradeshop tradeshop,
                           OrderService orderService) {
        this.tradeshop = tradeshop;
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.debug("Received new order request: {} {}", orderRequest.getUserId(), orderRequest.getName());
        return tradeshop.placeOrder(orderRequest);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.orderList();
    }
}
