package com.netprecision.lanchonete.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProdutoDTO {
    @NotNull(message = "o campo name não pode ser nulo")
    private String name;

    @NotNull(message = "o campo price não pode ser nulo")
    private float price;
}
