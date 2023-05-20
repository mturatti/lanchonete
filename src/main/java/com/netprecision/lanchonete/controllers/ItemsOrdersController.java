package com.netprecision.lanchonete.controllers;


import com.netprecision.lanchonete.dtos.AddItemDTO;
import com.netprecision.lanchonete.dtos.RemoveItemDTO;
import com.netprecision.lanchonete.services.ItemsOrdersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items_orders")
//@AllArgsConstructor
public class ItemsOrdersController {
    @Autowired
    private ItemsOrdersServices itemsOrdersServices;

    @PostMapping("/add_item/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToOrder(@PathVariable Integer id, @RequestBody AddItemDTO addItemDTO){
        itemsOrdersServices.addItemToOrder(id, addItemDTO);
    }

    @PutMapping("/remove_item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItemToOrder(@PathVariable Integer id, @RequestBody RemoveItemDTO removeItemDTO){
        itemsOrdersServices.removeItemToOrder(id, removeItemDTO);
    }
}
