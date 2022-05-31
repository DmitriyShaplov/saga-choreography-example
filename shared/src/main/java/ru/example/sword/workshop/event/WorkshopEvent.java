package ru.example.sword.workshop.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.sword.workshop.model.Product;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkshopEvent {
    private UUID id = UUID.randomUUID();
    private Instant instant = Instant.now();
    private Product product;

    public WorkshopEvent(Product product) {
        this.product = product;
    }
}
