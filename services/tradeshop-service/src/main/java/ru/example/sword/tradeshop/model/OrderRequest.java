package ru.example.sword.tradeshop.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String name;
}
