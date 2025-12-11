package com.example.tepinhui;

public class RefundItem {
    private String refundId;
    private String type;
    private String productName;
    private double amount;
    private String status;

    public RefundItem(String refundId, String type, String productName, double amount, String status) {
        this.refundId = refundId;
        this.type = type;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
    }

    public String getRefundId() { return refundId; }
    public String getType() { return type; }
    public String getProductName() { return productName; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
}