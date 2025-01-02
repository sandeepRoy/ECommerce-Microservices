package com.sandeep.pdfgenerator.controller;

import com.sandeep.pdfgenerator.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/invoicing")
public class InvoiceController {

    @Autowired
    public InvoiceService invoiceService;

    @GetMapping("/get-invoice")
    public ResponseEntity<byte[]> getPDF() throws IOException {

        byte[] pdf = invoiceService.getPDF();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdf, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/create-pdf")
    public ResponseEntity<String> createPDF() {
        System.out.println("I've been called by Customer-Service");
        String response = invoiceService.createPDF();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
