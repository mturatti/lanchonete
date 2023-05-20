package com.netprecision.lanchonete.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveItemDTO {
    @NotNull(message = "o campo pedidoId não pode ser nulo")
    private Integer productId;

    @NotNull(message = "o campo quantidade não pode ser nulo")
    private Integer amount;
}
