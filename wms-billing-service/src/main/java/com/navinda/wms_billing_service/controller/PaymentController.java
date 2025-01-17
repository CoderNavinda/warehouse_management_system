package com.navinda.wms_billing_service.controller;

import com.navinda.wms_billing_service.model.Payment;
import com.navinda.wms_billing_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Payment processPayment(@RequestParam Long invoiceId, @RequestParam String paymentMode) {
        return paymentService.processPayment(invoiceId, paymentMode);
    }
}
