package com.msa.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.msa.order.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
