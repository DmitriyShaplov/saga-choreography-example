package ru.example.sword.delivery.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.example.sword.delivery.event.DeliveryEvent;
import ru.example.sword.delivery.service.DeliveryService;
import ru.example.sword.workshop.event.WorkshopEvent;
import ru.example.sword.workshop.model.ProductState;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@Slf4j
public class StreamConfiguration {

    private final DeliveryService deliveryService;

    public StreamConfiguration(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Bean
    public Function<KStream<UUID, WorkshopEvent>, KStream<UUID, DeliveryEvent>> processProduct() {
        return input -> input
                .filter((key, value) -> ProductState.COMPLETE.equals(value.getProduct().getState()))
                .peek((key, value) -> log.info("Incoming workshop event {} {}",
                        value.getProduct().getOrderId(), value.getProduct().getNaming()))
                .mapValues(WorkshopEvent::getProduct)
                .mapValues(deliveryService::processProduct)
                .mapValues(DeliveryEvent::new);
    }
}
