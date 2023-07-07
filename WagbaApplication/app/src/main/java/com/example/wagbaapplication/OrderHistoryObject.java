package com.example.wagbaapplication;

public class OrderHistoryObject {
    String DeliveryTime;
    String GateNumber;
    String OrderTime;
    String OrderUserId;
    String Status;
    String TotalAmount;
    String PaymentMethod;

    public OrderHistoryObject(String deliveryTime, String gateNumber, String orderTime, String orderUserId, String status, String totalAmount, String paymentMethod) {
        DeliveryTime = deliveryTime;
        GateNumber = gateNumber;
        OrderTime = orderTime;
        OrderUserId = orderUserId;
        Status = status;
        TotalAmount = totalAmount;
        PaymentMethod = paymentMethod;
    }

    public OrderHistoryObject() {
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getGateNumber() {
        return GateNumber;
    }

    public void setGateNumber(String gateNumber) {
        GateNumber = gateNumber;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderUserId() {
        return OrderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        OrderUserId = orderUserId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}