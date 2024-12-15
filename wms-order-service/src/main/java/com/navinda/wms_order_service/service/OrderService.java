package com.navinda.wms_order_service.service;

import com.navinda.wms_order_service.model.Order;
import com.navinda.wms_order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;

    public Order createOrder(Order order) {
        // Check inventory
        String checkUrl = inventoryServiceUrl + "/inventory/check/" + order.getSku() + "/" + order.getQuantity();
        ResponseEntity<Boolean> response = restTemplate.getForEntity(checkUrl, Boolean.class);

        if (Boolean.TRUE.equals(response.getBody())) {
            // Reduce inventory
            String reduceUrl = inventoryServiceUrl + "/inventory/reduce";
            Map<String, Object> reduceRequest = Map.of("sku", order.getSku(), "quantity", order.getQuantity());
            restTemplate.put(reduceUrl, reduceRequest);

            // Save the order
            order.setStatus("Processed");
            Order savedOrder = orderRepository.save(order);

            // Create shipment
            createShipmentForOrder(savedOrder);

            return savedOrder;

        } else {
            throw new IllegalStateException("Insufficient stock");
        }
    }

    private void createShipmentForOrder(Order order) {
        try {
            String shipmentUrl = shippingServiceUrl + "/shipments/create";
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(shipmentUrl)
                    .queryParam("orderId", order.getId())
                    .queryParam("carrier", "Default Carrier")
                    .queryParam("estimatedDeliveryDate", LocalDate.now().plusDays(5).toString());

            restTemplate.postForEntity(uriBuilder.toUriString(), null, Void.class);
        } catch (HttpClientErrorException ex) {
            // Log error
            System.err.println("Failed to create shipment: " + ex.getMessage());
            // Optional: Update order status to indicate shipment failure
            order.setStatus("Shipment Failed");
            orderRepository.save(order);
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        } else {
            return null;
        }
    }
}