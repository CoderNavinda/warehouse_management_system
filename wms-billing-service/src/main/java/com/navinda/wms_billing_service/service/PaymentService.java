package com.navinda.wms_billing_service.service;

import com.navinda.wms_billing_service.model.Payment;
import com.navinda.wms_billing_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(Long invoiceId, String paymentMode) {
        Payment payment = new Payment();
        payment.setInvoiceId(invoiceId);
        payment.setPaymentMode(paymentMode);
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
