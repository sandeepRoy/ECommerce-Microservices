package com.msa.payment.repositories;

import com.msa.payment.entities.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Integer> {

}
