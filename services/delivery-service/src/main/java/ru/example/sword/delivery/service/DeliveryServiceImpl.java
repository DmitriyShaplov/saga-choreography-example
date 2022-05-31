package ru.example.sword.delivery.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.sword.delivery.model.DeliverableProduct;
import ru.example.sword.delivery.model.DeliveryState;
import ru.example.sword.workshop.model.Product;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    @Override
    @SneakyThrows
    public DeliverableProduct processProduct(Product product) {
        printInfo("Delivering product.", product);
        DeliverableProduct deliverableProduct = new DeliverableProduct(product, DeliveryState.DELIVERING);
        Thread.sleep(500);
        if ("lost".equals(product.getNaming())) {
            printInfo("Lost product!", product);
            deliverableProduct.setState(DeliveryState.LOST);
            return deliverableProduct;
        } else {
            printInfo("Almost complete..", product);
        }
        Thread.sleep(500);
        printInfo("Delivered!", product);
        return deliverableProduct.setState(DeliveryState.COMPLETE);
    }

    private void printInfo(String phrase, Product product) {
        log.info(phrase + " {} {}", product.getOrderId(), product.getNaming());
    }
}
