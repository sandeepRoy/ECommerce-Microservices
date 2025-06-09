package com.sandeep.pdfgenerator.controller;

import com.sandeep.pdfgenerator.response.InvoiceResponse;
import com.sandeep.pdfgenerator.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/invoicing")
public class InvoiceController {

    @Autowired
    public InvoiceService invoiceService;

    @PostMapping("/get-invoice")
    public ResponseEntity<byte[]> getPDF(@RequestBody InvoiceResponse invoiceResponse) throws IOException {

        byte[] pdf = invoiceService.getPDF(invoiceResponse);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdf, httpHeaders, HttpStatus.OK);
    }
}
