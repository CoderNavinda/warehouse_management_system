package com.navinda.wms_notification_service.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderEventListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order_created")
    public void handleOrderCreated(String message) {
        try {

            Map<String, String> event = objectMapper.readValue(message,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

            String orderId = event.get("orderId");
            String customerEmail = event.get("customerEmail");

            System.out.println("Sending notification for Order ID: " + orderId + " to " + customerEmail);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Failed to process Kafka message: " + message);
        }
    }
}
