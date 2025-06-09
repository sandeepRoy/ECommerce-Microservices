package com.sandeep.pdfgenerator.client;

import com.sandeep.pdfgenerator.response.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customerInvoice", url  = "http://localhost:8087/customer/cart")
public interface CustomerInvoiceClient {
    @GetMapping("/get-invoice")
    public ResponseEntity<InvoiceResponse> get_invoice(@RequestHeader("Authorization") String access_token);
}
