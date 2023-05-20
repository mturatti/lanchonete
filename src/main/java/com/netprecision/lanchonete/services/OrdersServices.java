package com.netprecision.lanchonete.services;

import com.netprecision.lanchonete.dtos.CreateOrderDTO;
import com.netprecision.lanchonete.dtos.CloseOrderDTO;
import com.netprecision.lanchonete.dtos.ItemOrderDTO;
import com.netprecision.lanchonete.dtos.ValueOfTheOrderDTO;
import com.netprecision.lanchonete.dtos.ValueOfTheOrderDetailsDTO;
import com.netprecision.lanchonete.entities.Order;
import com.netprecision.lanchonete.entities.ItemOrder;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.enums.OrderStatus;
import com.netprecision.lanchonete.exceptions.AssignedValueException;
import com.netprecision.lanchonete.exceptions.NotFoundException;
import com.netprecision.lanchonete.repositories.ItemsOrdersRepository;
import com.netprecision.lanchonete.repositories.OrdersRepository;
import com.netprecision.lanchonete.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServices {
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final ItemsOrdersRepository itemsOrdersRepository;

    public Order create(CreateOrderDTO createOrderDTO){
        Order order = new Order();
        order.setRequestDate(LocalDate.now());
        order.setStatus(OrderStatus.OPEN);

        List<ItemOrder> itensPedido = convertItems(order, createOrderDTO.getItems());

        ordersRepository.save(order);
        itemsOrdersRepository.saveAll(itensPedido);
        order.setItems(itensPedido);
        return order;
    }

    public List<Order> listAll() {
        return ordersRepository.findAll();
    }

    public Order findOne(Integer id) {
        return ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Código de pedido inválido: "+ id ));
    }

    public ValueOfTheOrderDTO orderValue(Integer id) {
        Order order = ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Código de pedido inválido: "+ id ));

        float valor = 0;

        for (ItemOrder itemOrder : order.getItems()) {
            valor += itemOrder.getAmount() * itemOrder.getProduct().getPrice();
        }

        ValueOfTheOrderDTO valueOfTheOrderDTO = new ValueOfTheOrderDTO();
        valueOfTheOrderDTO.setValueOfTheOrder(valor);

        return valueOfTheOrderDTO;
    }

    public ValueOfTheOrderDTO orderValue(Integer id, ValueOfTheOrderDetailsDTO valueOfTheOrderDetailsDTO) {
        // TODO Pedido pedido = pedidosRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Código de pedido inválido: "+ id ));
        float valor = 0;

        for (ItemOrderDTO itemOrderDTO : valueOfTheOrderDetailsDTO.getItems()) {
            Product product = productsRepository.findById(itemOrderDTO.getProductId()).orElseThrow(() -> new NotFoundException("Código de produto inválido: "+ id ));

            if (itemOrderDTO.getAmount() <= 0) {
                throw new AssignedValueException("A quantidade não pode ser menor igual a 0");
            }

            valor += itemOrderDTO.getAmount() * product.getPrice();
        }

        ValueOfTheOrderDTO valueOfTheOrderDTO = new ValueOfTheOrderDTO();
        valueOfTheOrderDTO.setValueOfTheOrder(valor);

        return valueOfTheOrderDTO;
    }

    public Order closeOrder(Integer id, CloseOrderDTO closeOrderDTO) {
        Order order = ordersRepository.findById(id).orElseThrow(() -> new NotFoundException("Código de produto inválido: "+ id));

        order.setStatus(OrderStatus.CLOSED);
        order.setTotal(closeOrderDTO.getTotal());

        return ordersRepository.save(order);
    }

    private List<ItemOrder> convertItems(Order order, List<ItemOrderDTO> items){
        if(items.isEmpty()){
            throw new AssignedValueException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    if (dto.getAmount() <= 0) {
                        throw new AssignedValueException("A quantidade não pode ser menor igual a 0");
                    }

                    Integer idProduto = dto.getProductId();
                    Product product = productsRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new NotFoundException("Código de produto inválido: "+ idProduto));

                    ItemOrder itemOrder = new ItemOrder();
                    itemOrder.setAmount(dto.getAmount());
                    itemOrder.setOrder(order);
                    itemOrder.setProduct(product);
                    return itemOrder;
                }).collect(Collectors.toList());

    }
}
