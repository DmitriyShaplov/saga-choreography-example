package ru.example.sword.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.sword.workshop.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableProduct {
    private Product product;
    private DeliveryState state;
}
