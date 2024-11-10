package com.msa.payment.services;

import com.msa.payment.dtos.PaymentOrderRequest;
import com.msa.payment.entities.PaymentOrder;
import com.msa.payment.repositories.PaymentOrderRepository;
import com.msa.payment.response.OrderResponse;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PaymentService {

    public static final String SECRET_KEY = "cKNpYuq49z28DN+sH1FpDVLWX4vMd12QGWHx62oj3BGrQ4uNCr4Yxm5St3/P5dMUgVt3aK/0BK+zuqnQHMYZ1xAUMTpV09YCXimbAP2SYkUlZqI1XbIT5Idxsdu41xZ+VZAH3h6EZ+2WdrzezxJJ30URiyHu7bgGMPwoQjxidd5HR0uv7BVhD9xxEkI4jgWaZl0i9uKAZSqFfTaCTKCUzbK/COBQbj1SUQ7qT30XBTSdla+lK04wLAaJeiyGoXxnNFfMlS20uzmBJba8AdHxpmMmajptR8BdAUf+2HaX2MSHCzZRHXNwuW7mxFbDrMl0JpCAABBSMd7E51GaDnA1Vjcao7rzFuLVCXzkNt8P4F4";

    public static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Autowired
    public PaymentOrderRepository paymentOrderRepository;


    @Value("${razorpay.key.id}")
    private String razorpay_key;

    @Value("${razorpay.secret.key}")
    private String razorpay_secret;

    private RazorpayClient razorpayClient;

    public PaymentOrder createPaymentOrder(PaymentOrderRequest paymentOrderRequest) throws RazorpayException {

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setName(paymentOrderRequest.getName());
        paymentOrder.setEmail(paymentOrderRequest.getEmail());
        paymentOrder.setPhone(paymentOrderRequest.getPhone());
        paymentOrder.setAmount(paymentOrderRequest.getAmount());
        paymentOrder.setDelivery_address(paymentOrderRequest.getDelivery_address());

        JSONObject order_request = new JSONObject();

        order_request.put("amount", paymentOrderRequest.getAmount().intValue() * 100);
        order_request.put("currency", "INR");
        order_request.put("receipt", paymentOrderRequest.getEmail());

        this.razorpayClient = new RazorpayClient(razorpay_key, razorpay_secret);

        // create a payment_order in razorpay
        Order razorpay_order = razorpayClient.orders.create(order_request);

        System.out.println(razorpay_order);

        paymentOrder.setRazorpay_order_id(razorpay_order.get("id"));
        paymentOrder.setOrder_status(razorpay_order.get("status"));

        paymentOrderRepository.save(paymentOrder);

        return paymentOrder;
    }

    public PaymentOrder updateOrder(Map<String, String> responsePayload) {
        String razorpayOrderId = responsePayload.get("razorpay_order_id");
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setRazorpay_order_id(razorpayOrderId);

        Example<PaymentOrder> paymentOrderExample = Example.of(paymentOrder);
        PaymentOrder found_paymentOrder = paymentOrderRepository.findOne(paymentOrderExample).get();

        found_paymentOrder.setOrder_status("PAYMENT_COMPLETED");
        PaymentOrder paymentOrder_statusUpdated = paymentOrderRepository.save(found_paymentOrder);

        return paymentOrder_statusUpdated;
    }
}
