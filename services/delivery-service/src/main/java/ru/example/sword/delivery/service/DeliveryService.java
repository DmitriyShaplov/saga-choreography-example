package ru.example.sword.delivery.service;

import ru.example.sword.delivery.model.DeliverableProduct;
import ru.example.sword.workshop.model.Product;

public interface DeliveryService {
    DeliverableProduct processProduct(Product product);
}
