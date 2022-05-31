package ru.example.sword.workshop.service;

import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.workshop.model.Product;

public interface Blacksmith {
    Product processOrder(Order order);

    void decreaseStatistic();
}
