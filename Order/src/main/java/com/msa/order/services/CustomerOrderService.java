package com.msa.order.services;

import com.msa.order.clients.PaymentClient;
import com.msa.order.entities.Order;
import com.msa.order.repositories.OrderRepository;
import com.msa.order.responses.PaymentOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {

    public static final Logger logger = Logger.getLogger(CustomerOrderService.class.getName());

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public PaymentClient paymentClient;


    public Order generateCustomerOrder() {
        PaymentOrderResponse paymentOrderResponse = paymentClient.getPaymentOrder().getBody();
        Order order = Order
                .builder()
                .customer_name(paymentOrderResponse.getName())
                .customer_phone(paymentOrderResponse.getPhone())
                .customer_email(paymentOrderResponse.getEmail())
                .customer_delivery_address(paymentOrderResponse.getDelivery_address())
                .amount(paymentOrderResponse.getAmount())
                .expected_delivery_date(LocalDate.from(LocalDate.now()).plusDays(7))
                .status("ORDER_GENERATED")
                .build();

        logger.info("Order: " + order.toString());

        orderRepository.save(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
