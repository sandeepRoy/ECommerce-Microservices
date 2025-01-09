package com.msa.order.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.order.clients.CustomerClient;
import com.msa.order.clients.EmailingClient;
import com.msa.order.entities.Order;
import com.msa.order.repositories.OrderRepository;
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

    public String sendEmaill() throws IOException {

        logger.info("sendEmail() called");
        // get invoice.json & invoice.pdf from customer
        InvoiceResponse invoiceResponse = customerClient.get_invoice().getBody(); logger.info("Invoice.json -- " + invoiceResponse);
        byte[] invoicePDF = customerClient.download_invoice().getBody(); logger.info("Invoice.pdf -- " + invoicePDF);

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


    private MultipartFile convertByteFiletoMultipartFile(byte[] invoicePDF) {
        String name = "invoice";
        String originalName = "invoice.pdf";
        String contentType = MediaType.APPLICATION_PDF_VALUE;

        ByteToMultipartConverter byteToMultipartConverter = new ByteToMultipartConverter(); // helper class
        MultipartFile multipartFile = byteToMultipartConverter.convertByteToMultipart(invoicePDF, name, originalName, contentType);
        return multipartFile;
    }
}
