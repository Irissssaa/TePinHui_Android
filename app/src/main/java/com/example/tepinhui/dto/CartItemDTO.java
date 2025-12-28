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

    // setters (用于前端同步/回滚)
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setChecked(boolean checked) { this.checked = checked; }
}
