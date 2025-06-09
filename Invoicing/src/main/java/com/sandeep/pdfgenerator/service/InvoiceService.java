package com.sandeep.pdfgenerator.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.sandeep.pdfgenerator.client.CustomerInvoiceClient;
import com.sandeep.pdfgenerator.response.CustomerPurchase;
import com.sandeep.pdfgenerator.response.InvoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class InvoiceService {

    private static Logger logger = Logger.getLogger(InvoiceService.class.getName());

    @Autowired
    public CustomerInvoiceClient customerInvoiceClient;

    public byte[] getPDF(InvoiceResponse invoiceResponse) throws IOException {
        logger.info("Reached /invoicing/get-invoice -> getPDF()");

        PdfFont customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // InvoiceResponse invoiceResponse = customerInvoiceClient.get_invoice(access_token).getBody();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String pdf_location = "src/main/resources/templates/invoice.pdf";

        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        // heading
        document.add(new Paragraph("ECMS - Invoice").setTextAlignment(TextAlignment.CENTER).setBold());

        document.add(
                new Paragraph("Date : " + invoiceResponse.getInvoice_generationDate()).setTextAlignment(TextAlignment.LEFT)
        ).add(
                new Paragraph("Invoice " + invoiceResponse.getInvoice_number()).setTextAlignment(TextAlignment.RIGHT)
        );

        document.add(new LineSeparator(new DashedLine()).setBold());

        // body
        // Customer Details
        document.add(new Paragraph("\nShipping Details").setBold().setFontSize(14));
        Table customerTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
        customerTable.addCell(new Cell().add(new Paragraph("Name").setBold()));
        customerTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getCustomer_name())));
        customerTable.addCell(new Cell().add(new Paragraph("Email").setBold()));
        customerTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getCustomer_email())));
        customerTable.addCell(new Cell().add(new Paragraph("Phone").setBold()));
        customerTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getCustomer_phone())));
        customerTable.addCell(new Cell().add(new Paragraph("Delivery Address").setBold()));
        customerTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getCustomer_delivery_address())));
        document.add(customerTable);

        // Order Details
        document.add(new Paragraph("\nOrder Details").setBold().setFontSize(14));
        Table orderTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 1, 1})).useAllAvailableWidth();
        orderTable.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()));
        orderTable.addHeaderCell(new Cell().add(new Paragraph("Manufacturer").setBold()));
        orderTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()));
        orderTable.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()));

        for (CustomerPurchase purchase : invoiceResponse.getCustomerOrder().getCustomer_purchase()) {
            orderTable.addCell(new Cell().add(new Paragraph(purchase.getProduct_name())));
            orderTable.addCell(new Cell().add(new Paragraph(purchase.getProduct_manufacturer())));
            orderTable.addCell(new Cell().add(new Paragraph(String.valueOf(purchase.getProduct_quantity()))));
            orderTable.addCell(new Cell().add(new Paragraph("₹" + purchase.getProduct_price())));
        }
        document.add(orderTable);

        // Order Summary
        document.add(new Paragraph("\nOrder Summary").setBold().setFontSize(14));
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        summaryTable.addCell(new Cell().add(new Paragraph("Total Amount").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph("₹" + invoiceResponse.getCustomerOrder().getAmount())));
        summaryTable.addCell(new Cell().add(new Paragraph("Order Date").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getOrder_date())));
        summaryTable.addCell(new Cell().add(new Paragraph("Payment Date").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getPayment_date())));
        summaryTable.addCell(new Cell().add(new Paragraph("Expected Delivery").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph(invoiceResponse.getCustomerOrder().getExpected_delivery_date())));
        document.add(summaryTable);

        // Customer Service
        // Footer Section - Customer Service Details
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i));
            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();

            Canvas footerCanvas = new Canvas(canvas, pdfDocument, pageSize);
            footerCanvas.showTextAligned(new Paragraph("For Customer Support, contact us at support@ecms.com or call +91 12345 67890")
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER),
                    pageSize.getWidth() / 2,
                    pageSize.getBottom() + 20, // Adjust for footer position
                    TextAlignment.CENTER);
            footerCanvas.close();
        }

        // Footer
        document.add(new Paragraph("\nThank you for your purchase!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setItalic());

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
