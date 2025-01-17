package com.navinda.wms_billing_service.service;

import com.navinda.wms_billing_service.model.Invoice;
import com.navinda.wms_billing_service.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice createInvoice(Long orderId, Double amount) {
        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setAmount(amount);
        invoice.setStatus(Invoice.Status.PENDING);
        invoice.setCreatedAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoiceStatus(Long invoiceId, Invoice.Status status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice not found"));
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
