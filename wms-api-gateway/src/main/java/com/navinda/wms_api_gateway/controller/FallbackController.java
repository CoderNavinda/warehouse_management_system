package com.navinda.wms_api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/inventoryFallback")
    public String inventoryFallback() {
        return "Inventory Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/orderFallback")
    public String orderFallback() {
        return "Order Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/shippingFallback")
    public String shippingFallback() {
        return "Shipping Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/billingFallback")
    public String billingFallback() {
        return "billing Service is currently unavailable. Please try again later.";
    }
}