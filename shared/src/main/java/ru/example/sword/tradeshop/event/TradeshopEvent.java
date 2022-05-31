package ru.example.sword.tradeshop.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.sword.tradeshop.model.Order;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeshopEvent {
    private UUID id = UUID.randomUUID();
    private Instant instant = Instant.now();
    private Order order;

    public TradeshopEvent(Order order) {
        this.order = order;
    }
}
