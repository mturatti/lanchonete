package com.netprecision.lanchonete.repositories;

import com.netprecision.lanchonete.entities.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsOrdersRepository extends JpaRepository<ItemOrder, Integer> {
    ItemOrder findByOrderIdAndProductId(Integer orderId, Integer ProductId);
}
