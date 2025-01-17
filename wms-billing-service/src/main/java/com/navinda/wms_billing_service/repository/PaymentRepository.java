package com.navinda.wms_billing_service.repository;

import com.navinda.wms_billing_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
