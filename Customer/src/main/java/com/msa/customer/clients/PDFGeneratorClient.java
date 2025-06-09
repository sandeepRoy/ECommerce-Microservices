package com.msa.customer.clients;

import com.msa.customer.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@FeignClient(name = "invoicing", url = "http://localhost:8090/invoicing")
public interface PDFGeneratorClient {
    @PostMapping("/get-invoice")
    public ResponseEntity<byte[]> getPDF(Invoice invoice) throws IOException;
}
