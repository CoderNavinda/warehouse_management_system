package com.navinda.wms_order_service.service;

import com.navinda.wms_order_service.model.Order;
import com.navinda.wms_order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Order createOrder(Order order) {
        // Check inventory
        String checkUrl = inventoryServiceUrl + "/inventory/check/" + order.getSku() + "/" + order.getQuantity();
        ResponseEntity<Boolean> response = restTemplate.getForEntity(checkUrl, Boolean.class);

        if (Boolean.TRUE.equals(response.getBody())) {
            // Reduce inventory
            String reduceUrl = inventoryServiceUrl + "/inventory/reduce";
            Map<String, Integer> reduceRequest = Map.of("sku", Integer.valueOf(order.getSku()), "quantity",
                    order.getQuantity());
            restTemplate.put(reduceUrl, reduceRequest);

            // Save the order
            order.setStatus("Processed");
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Insufficient stock");
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