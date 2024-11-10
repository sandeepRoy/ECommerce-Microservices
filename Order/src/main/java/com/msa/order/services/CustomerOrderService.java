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

    public static final String SECRET_KEY = "cKNpYuq49z28DN+sH1FpDVLWX4vMd12QGWHx62oj3BGrQ4uNCr4Yxm5St3/P5dMUgVt3aK/0BK+zuqnQHMYZ1xAUMTpV09YCXimbAP2SYkUlZqI1XbIT5Idxsdu41xZ+VZAH3h6EZ+2WdrzezxJJ30URiyHu7bgGMPwoQjxidd5HR0uv7BVhD9xxEkI4jgWaZl0i9uKAZSqFfTaCTKCUzbK/COBQbj1SUQ7qT30XBTSdla+lK04wLAaJeiyGoXxnNFfMlS20uzmBJba8AdHxpmMmajptR8BdAUf+2HaX2MSHCzZRHXNwuW7mxFbDrMl0JpCAABBSMd7E51GaDnA1Vjcao7rzFuLVCXzkNt8P4F4";

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
