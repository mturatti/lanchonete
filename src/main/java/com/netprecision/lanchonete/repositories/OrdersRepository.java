package com.netprecision.lanchonete.repositories;

import com.netprecision.lanchonete.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Integer> {}
