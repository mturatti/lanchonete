package com.netprecision.lanchonete.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    @NotEmpty(message = "o campo items não deve estar vazio")
    @NotBlank(message = "o campo items não deve estar em branco")
    private List<ItemOrderDTO> items;
}
