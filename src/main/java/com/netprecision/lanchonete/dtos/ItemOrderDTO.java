package com.netprecision.lanchonete.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderDTO {
    private Integer productId;
    private Integer amount;
}
