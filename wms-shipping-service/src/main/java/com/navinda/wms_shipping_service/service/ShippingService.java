package com.navinda.wms_shipping_service.service;

import com.navinda.wms_shipping_service.model.*;
import com.navinda.wms_shipping_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShippingService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public Shipment createShipment(Long orderId, String carrier, LocalDate estimatedDeliveryDate) {
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setTrackingNumber(UUID.randomUUID().toString()); // Generate a unique tracking number
        shipment.setCarrier(carrier);
        shipment.setStatus("Pending");
        shipment.setShippingDate(LocalDate.now());
        shipment.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> getShipmentById(Long id) {
        return shipmentRepository.findById(id);
    }

    public Shipment updateShipmentStatus(Long id, String status) {
        Optional<Shipment> optionalShipment = shipmentRepository.findById(id);
        if (optionalShipment.isPresent()) {
            Shipment shipment = optionalShipment.get();
            shipment.setStatus(status);
            return shipmentRepository.save(shipment);
        }
        return null;
    }
}
