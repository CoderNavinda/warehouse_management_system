package com.navinda.wms_shipping_service.controller;

import com.navinda.wms_shipping_service.model.Shipment;
import com.navinda.wms_shipping_service.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("/create")
    public ResponseEntity<Shipment> createShipment(@RequestParam Long orderId,
            @RequestParam String carrier,
            @RequestParam LocalDate estimatedDeliveryDate) {
        Shipment shipment = shippingService.createShipment(orderId, carrier, estimatedDeliveryDate);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments() {
        return ResponseEntity.ok(shippingService.getAllShipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Long id) {
        return shippingService.getShipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
        Shipment updatedShipment = shippingService.updateShipmentStatus(id, status);
        return updatedShipment != null ? ResponseEntity.ok(updatedShipment) : ResponseEntity.notFound().build();
    }
}
