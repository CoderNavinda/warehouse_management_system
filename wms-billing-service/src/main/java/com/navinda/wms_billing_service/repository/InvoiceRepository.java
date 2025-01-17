package com.navinda.wms_billing_service.repository;

import com.navinda.wms_billing_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
