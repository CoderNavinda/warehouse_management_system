package com.navinda.wms_order_service.event;

public class OrderCreatedEvent {
    private Long orderId;
    private String customerEmail;
    private String status;

    public OrderCreatedEvent(Long orderId, String customerEmail, String status) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
