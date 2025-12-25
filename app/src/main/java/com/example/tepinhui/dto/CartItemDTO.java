package com.example.tepinhui.dto;

public class CartItemDTO {
    private int productId;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;
    private boolean checked;

    // getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public boolean isChecked() { return checked; }
}
