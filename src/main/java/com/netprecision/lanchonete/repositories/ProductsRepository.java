package com.netprecision.lanchonete.repositories;

import com.netprecision.lanchonete.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Integer> {}
