package com.netprecision.lanchonete.services;

import com.netprecision.lanchonete.dtos.AddItemDTO;
import com.netprecision.lanchonete.dtos.RemoveItemDTO;
import com.netprecision.lanchonete.entities.ItemOrder;
import com.netprecision.lanchonete.entities.Order;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.enums.OrderStatus;
import com.netprecision.lanchonete.exceptions.AssignedValueException;
import com.netprecision.lanchonete.exceptions.ClosedOrderException;
import com.netprecision.lanchonete.exceptions.NotFoundException;
import com.netprecision.lanchonete.repositories.ItemsOrdersRepository;
import com.netprecision.lanchonete.repositories.OrdersRepository;
import com.netprecision.lanchonete.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsOrdersServices {

    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final ItemsOrdersRepository itemsOrdersRepository;
    public void addItemToOrder(Integer orderId, AddItemDTO addItemDTO) {
        Order order = ordersRepository
            .findById(orderId)
            .orElseThrow(() -> new NotFoundException("Código de pedido inválido: "+ orderId));

        if (order.getStatus() == OrderStatus.CLOSED) {
            throw new ClosedOrderException("o pedido ja está fechado");
        }

        Product product = productsRepository
            .findById(addItemDTO.getProductId())
            .orElseThrow(() -> new NotFoundException("Código de produto inválido: "+ addItemDTO.getProductId()));

        if (addItemDTO.getAmount() <= 0) {
            throw new AssignedValueException("A quantidade não pode ser menor igual a 0");
        }

        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setOrder(order);
        itemOrder.setProduct(product);
        itemOrder.setAmount(addItemDTO.getAmount());

        itemsOrdersRepository.save(itemOrder);
    }

    public void removeItemToOrder(Integer orderId, RemoveItemDTO removeItemDTO) {
        Order order = ordersRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException("Código de pedido inválido: "+ orderId));

        if (order.getStatus() == OrderStatus.CLOSED) {
            throw new ClosedOrderException("o pedido ja está fechado");
        }

        Product product = productsRepository
                .findById(removeItemDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Código de produto inválido: "+ removeItemDTO.getProductId()));

        if (removeItemDTO.getAmount() <= 0) {
            throw new AssignedValueException("A quantidade não pode ser menor igual a 0");
        }

        ItemOrder itemOrder = itemsOrdersRepository.findByOrderIdAndProductId(order.getId(), product.getId());

        if (removeItemDTO.getAmount() >= itemOrder.getAmount()){
            itemsOrdersRepository.delete(itemOrder);
        } else {
            itemOrder.setAmount(itemOrder.getAmount() - removeItemDTO.getAmount());
            itemsOrdersRepository.save(itemOrder);
        }
    }
}
