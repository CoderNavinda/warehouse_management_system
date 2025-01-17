package com.navinda.wms_order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navinda.wms_order_service.model.Order;
import com.navinda.wms_order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.navinda.wms_order_service.event.OrderCreatedEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private static final String ORDER_CREATED_TOPIC = "order_created";
    private static final String ORDER_STATUS_UPDATED_TOPIC = "order_status_updated";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;

    @Value("${billing.service.url}")
    private String billingServiceUrl;

    public Order createOrder(Order order) {

        String checkUrl = inventoryServiceUrl + "/inventory/check/" + order.getSku() + "/" + order.getQuantity();
        ResponseEntity<Boolean> response = restTemplate.getForEntity(checkUrl, Boolean.class);

        if (Boolean.TRUE.equals(response.getBody())) {

            String reduceUrl = inventoryServiceUrl + "/inventory/reduce";
            Map<String, Object> reduceRequest = Map.of("sku", order.getSku(), "quantity", order.getQuantity());
            restTemplate.put(reduceUrl, reduceRequest);

            order.setStatus("Processed");
            Order savedOrder = orderRepository.save(order);

            publishOrderCreatedEvent(savedOrder);

            createShipmentForOrder(savedOrder);

            createInvoiceForOrder(savedOrder);

            return savedOrder;
        } else {
            throw new IllegalStateException("Insufficient stock");
        }
    }

    private void createInvoiceForOrder(Order order) {
        try {
            String invoiceUrl = billingServiceUrl + "/invoices";
            Map<String, Object> invoiceRequest = Map.of(
                    "orderId", order.getId(),
                    "amount", order.getTotalAmount());

            restTemplate.postForEntity(invoiceUrl, invoiceRequest, Void.class);
            System.out.println("Invoice successfully generated for Order ID: " + order.getId());
        } catch (HttpClientErrorException ex) {
            System.err.println("Failed to generate invoice: " + ex.getMessage());
            order.setStatus("Invoice Failed");
            orderRepository.save(order);
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
            System.err.println("Failed to create shipment: " + ex.getMessage());
            order.setStatus("Shipment Failed");
            orderRepository.save(order);
        }
    }

    private void publishOrderCreatedEvent(Order order) {
        try {
            OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getCustomerEmail(), order.getStatus());
            String eventMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ORDER_CREATED_TOPIC, eventMessage);
            System.out.println("Order Created Event Published: " + eventMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to publish Order Created Event.");
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
            Order updatedOrder = orderRepository.save(order);

            publishOrderStatusUpdatedEvent(updatedOrder);

            return updatedOrder;
        } else {
            return null;
        }
    }

    private void publishOrderStatusUpdatedEvent(Order order) {
        try {
            Map<String, String> event = Map.of(
                    "orderId", String.valueOf(order.getId()),
                    "status", order.getStatus());
            String eventMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ORDER_STATUS_UPDATED_TOPIC, eventMessage);
            System.out.println("Order Status Updated Event Published: " + eventMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to publish Order Status Updated Event.");
        }
    }
}
