package com.msa.customer.repositories;

import com.msa.customer.model.CustomerPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPurchaseRepository extends JpaRepository<CustomerPurchase, Integer> {
}
