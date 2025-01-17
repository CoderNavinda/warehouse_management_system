package com.navinda.wms_billing_service.controller;

import com.navinda.wms_billing_service.model.Invoice;
import com.navinda.wms_billing_service.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice.getOrderId(), invoice.getAmount());
    }

    @PutMapping("/{id}/status")
    public Invoice updateInvoiceStatus(@PathVariable Long id, @RequestParam Invoice.Status status) {
        return invoiceService.updateInvoiceStatus(id, status);
    }
}
