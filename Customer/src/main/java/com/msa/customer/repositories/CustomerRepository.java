package com.msa.customer.repositories;

import com.msa.customer.model.Customer;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("select customer from Customer customer where customer.customer_mobile = :mobile")
    Optional<Customer> findByCustomerMobile(@Param("mobile") String mobile);
}
