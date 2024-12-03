package com.msa.order.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.order.clients.CustomerClient;
import com.msa.order.entities.Order;
import com.msa.order.repositories.OrderRepository;
import com.msa.order.responses.PaymentOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CustomerOrderService {

    public static final Logger logger = Logger.getLogger(CustomerOrderService.class.getName());

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public CustomerClient customerClient;

    public static Order order;

    @KafkaListener(topics = "paymentOrder" , groupId = "ecommerce", containerFactory = "kafkaListenerContainerFactory")
    public Order generateCustomerOrder(PaymentOrder paymentOrder) {

        order = Order
                .builder()
                .razorpay_order_id(paymentOrder.getRazorpay_order_id())
                .customer_name(paymentOrder.getName())
                .customer_phone(paymentOrder.getPhone())
                .customer_email(paymentOrder.getEmail())
                .customer_delivery_address(paymentOrder.getDelivery_address())
                .amount(paymentOrder.getAmount())
                .expected_delivery_date(LocalDate.from(LocalDate.now()).plusDays(7))
                .status("ORDER_GENERATED")
                .build();

        logger.info("Order: " + order.toString());

        orderRepository.save(order);
        return order;
    }
}
