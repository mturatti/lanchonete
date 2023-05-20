package com.netprecision.lanchonete.controllers;

import com.netprecision.lanchonete.dtos.CreateOrderDTO;
import com.netprecision.lanchonete.dtos.CloseOrderDTO;
import com.netprecision.lanchonete.dtos.ValueOfTheOrderDTO;
import com.netprecision.lanchonete.dtos.ValueOfTheOrderDetailsDTO;
import com.netprecision.lanchonete.entities.Order;
import com.netprecision.lanchonete.services.OrdersServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrdersController {
    private OrdersServices ordersServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateOrderDTO createOrderDTO){
        ordersServices.create(createOrderDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> listAll(){
        return ordersServices.listAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order findOne(@PathVariable Integer id){
        return ordersServices.findOne(id);
    }

    @GetMapping("/value_of_order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ValueOfTheOrderDTO valueOfTheOrderDTO(@PathVariable Integer id){
        return ordersServices.orderValue(id);
    }

    @PostMapping("/value_of_order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ValueOfTheOrderDTO valueOfTheOrderDTO(@PathVariable Integer id, @RequestBody ValueOfTheOrderDetailsDTO valueOfTheOrderDetailsDTO){
        return ordersServices.orderValue(id, valueOfTheOrderDetailsDTO);
    }

    @PutMapping("/close_order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeOrder(@PathVariable Integer id, @RequestBody CloseOrderDTO closeOrderDTO){
        ordersServices.closeOrder(id, closeOrderDTO);
    }
}
