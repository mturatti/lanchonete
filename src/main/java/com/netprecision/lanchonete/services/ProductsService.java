package com.netprecision.lanchonete.services;

import com.netprecision.lanchonete.dtos.CreateProdutoDTO;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    public Product create(CreateProdutoDTO createProdutoDTO){
        Product product = new Product();
        product.setName(createProdutoDTO.getName());
        product.setPrice(createProdutoDTO.getPrice());
        return productsRepository.save(product);
    }

    public List<Product> list(){
        return productsRepository.findAll();
    }
}
