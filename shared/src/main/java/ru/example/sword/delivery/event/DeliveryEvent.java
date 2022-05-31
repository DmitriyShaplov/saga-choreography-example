package ru.example.sword.delivery.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.sword.delivery.model.DeliverableProduct;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEvent {
    private UUID id = UUID.randomUUID();
    private Instant instant = Instant.now();
    private DeliverableProduct product;

    public DeliveryEvent(DeliverableProduct product) {
        this.product = product;
    }
}
