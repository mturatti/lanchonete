package com.netprecision.lanchonete.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseOrderDTO {
    @NotNull(message = "o campo total não pode ser nulo")
    private float total;
}
