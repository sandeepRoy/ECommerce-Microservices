package com.msa.order.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.order.clients.CustomerClient;
import com.msa.order.clients.EmailingClient;
import com.msa.order.clients.MessegingClient;
import com.msa.order.entities.Order;
import com.msa.order.repositories.OrderRepository;
import com.msa.order.requests.sms.SMSRequest;
import com.msa.order.responses.CartResponse;
import com.msa.order.responses.PaymentOrder;

import com.msa.order.responses.Wishlist;
import com.msa.order.responses.invoicing.CustomerPurchase;
import com.msa.order.responses.invoicing.InvoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.mock.web.MockMultipartFile;

@Service
public class CustomerOrderService {

    public static final Logger logger = Logger.getLogger(CustomerOrderService.class.getName());

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public CustomerClient customerClient;
    
    @Autowired
    public EmailingClient emailingClient;

    @Autowired
    public MessegingClient messegingClient;

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
                .payment_date(paymentOrder.getPayment_date())
                .order_date(LocalDate.now())
                .build();

        logger.info("Order: " + order.toString());

        orderRepository.save(order);

        // We need to send email to the customer about the order
        // fetch invoice.json & invoice.pdf from customer/cart/
        // send these data to Emailing for processing

        // sendEmaill();

        return order;
    }

    public String sendEmailTest() {
        MultipartFile mockFile = new MockMultipartFile(
                "attachment", "test.pdf", "application/pdf", "Test File Content".getBytes()
        );

        emailingClient.sendEmail("test@example.com", "Test Subject", "Test Body", mockFile);
        return "Email Sent!";

    }

    public String sendEmaill(String access_token, InvoiceResponse invoiceResponse) throws IOException {

        byte[] invoicePDF = customerClient.get_invoice(access_token).getBody();

        String customerEmail = invoiceResponse.getCustomerOrder().getCustomer_email(); //to
        String invoiceNumber = invoiceResponse.getInvoice_number(); //subject

        String subject = ""; // body

        ArrayList<CustomerPurchase> purchaseList = invoiceResponse.getCustomerOrder().getCustomer_purchase();
        for(CustomerPurchase customerPurchase : purchaseList) {
            subject += customerPurchase.getProduct_name() + "\t" + customerPurchase.getProduct_price() + "\t" + customerPurchase.getProduct_quantity() + "\n";
        }

        MultipartFile multipartFile = new MockMultipartFile("invoice", "invoice.pdf", "application/pdf", invoicePDF);

        logger.info("Mock Invoice: " + multipartFile.getName() + ", " + multipartFile.getOriginalFilename() + ", " + multipartFile.getContentType() + ", " + multipartFile.getBytes());

        // send the required data to emailing to send the email as attachment
        emailingClient.sendEmail(customerEmail, invoiceNumber, subject, multipartFile);

        return "Email Sent!";
    }

    public String sendSMS(String access_token, InvoiceResponse invoiceResponse) {

        logger.info("Invoice Response : " + invoiceResponse);

        String items = ""; // body
        ArrayList<CustomerPurchase> purchaseList = invoiceResponse.getCustomerOrder().getCustomer_purchase();
        for(CustomerPurchase customerPurchase : purchaseList) {
            items += customerPurchase.getProduct_name() + ", " + customerPurchase.getProduct_price() + ", " + customerPurchase.getProduct_quantity() + '\n';
        }

        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setPhone_number("+91" + invoiceResponse.getCustomerOrder().getCustomer_phone());
        smsRequest.setMessage(
                "Order " + invoiceResponse.getInvoice_number() + '\n' +
                "Items - " + items + "is Accepted. " + '\n' +
                "Expected Delivery Date - " + invoiceResponse.getCustomerOrder().getExpected_delivery_date() + '\n' +
                "Track Your Order at : " + "http://ecms.com/customer/track-order"
        );

        messegingClient.sendSMS(smsRequest);

        return "SMS Sent!";
    }


    private MultipartFile convertByteFiletoMultipartFile(byte[] invoicePDF) {
        String name = "invoice";
        String originalName = "invoice.pdf";
        String contentType = MediaType.APPLICATION_PDF_VALUE;

        ByteToMultipartConverter byteToMultipartConverter = new ByteToMultipartConverter(); // helper class
        MultipartFile multipartFile = byteToMultipartConverter.convertByteToMultipart(invoicePDF, name, originalName, contentType);
        return multipartFile;
    }
}
