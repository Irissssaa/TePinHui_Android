package com.example.tepinhui.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer productId;
    private String productName;
    private String productImg;
    private BigDecimal price;
    private Integer quantity;
    private Integer reviewStatus;

    // Getter和Setter
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImg() { return productImg; }
    public void setProductImg(String productImg) { this.productImg = productImg; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(Integer reviewStatus) { this.reviewStatus = reviewStatus; }
}