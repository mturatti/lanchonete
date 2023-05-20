package com.netprecision.lanchonete.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemDTO {
    @NotNull(message = "o campo orderId não pode ser nulo")
    private Integer productId;

    @NotNull(message = "o campo amount não pode ser nulo")
    private Integer amount;
}
