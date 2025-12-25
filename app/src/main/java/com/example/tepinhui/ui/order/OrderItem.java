package com.example.tepinhui.ui.order;

public class OrderItem {
    private String orderId;
    private String status;
    private String productName;
    private double amount;
    private String date;

    public OrderItem(String orderId, String status, String productName, double amount, String date) {
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.amount = amount;
        this.date = date;
    }

    // Getter 方法
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getProductName() { return productName; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
}