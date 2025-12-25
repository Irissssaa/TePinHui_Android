package com.example.tepinhui.ui.auth;

import com.example.tepinhui.dto.CartItemDTO;

import java.util.List;

public class CartDTO {
    private List<CartItemDTO> items;
    private double totalAmount;

    public List<CartItemDTO> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
