package ru.example.sword.workshop.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.sword.tradeshop.model.Order;
import ru.example.sword.workshop.model.Product;
import ru.example.sword.workshop.model.ProductState;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class BlacksmithImpl implements Blacksmith {

    private final Random random = new Random();
    private final AtomicLong totalOrderReceived = new AtomicLong();
    private final AtomicLong producedSuccessfully = new AtomicLong();

    @Override
    @SneakyThrows
    public Product processOrder(Order order) {
        long ordersTotal = totalOrderReceived.incrementAndGet();
        Product product = new Product(UUID.randomUUID(), order.getId(), order.getName(), ProductState.PROCESSING);
        int readyPercent = 0;
        log.debug("Start processing...{} {}", order.getId(), order.getName());
        while (readyPercent < 100) {
            Thread.sleep(200);
            readyPercent += random.nextInt(20, 50);
            if (readyPercent > 50 && "broke".equals(product.getNaming())) {
                log.info("The sword is broken! {} orderId={}", product.getNaming(), product.getOrderId());
                return product.setState(ProductState.BROKEN);
            }
            if (readyPercent > 100) {
                readyPercent = 100;
            }
            log.debug("processing sword... {}", readyPercent);
        }
        log.info("Successfully completed the manufacture of the sword. {} {}", product.getNaming(), product.getOrderId());
        long successfully = producedSuccessfully.incrementAndGet();
        log.info("Orders received: {}; Successfully completed: {}", ordersTotal, successfully);
        return product.setState(ProductState.COMPLETE);
    }

    @Override
    public void decreaseStatistic() {
        long total = totalOrderReceived.get();
        long success = producedSuccessfully.decrementAndGet();
        log.info("Oops someone lost our sword..");
        log.info("Order count/success ratio: {}/{}", total, success);
    }
}
