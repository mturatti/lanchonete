package com.netprecision.lanchonete.controllers;

import com.netprecision.lanchonete.dtos.CreateProdutoDTO;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductsController {
    private ProductsService productsService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateProdutoDTO createProdutoDTO){
        productsService.create(createProdutoDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> list(){
        return productsService.list();
    }
}
