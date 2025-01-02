package com.sandeep.pdfgenerator.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    public int invoice_id;
    public String invoice_number;
    public String invoice_generationDate;
    public CustomerOrder customerOrder;
}
