package com.example.tepinhui.ui.cart;

public class CartApi {

    public static String cart() {
        return "/api/cart";
    }

    public static String updateItem(int productId) {
        return "/api/cart/items/" + productId;
    }

    public static String deleteItem(int productId) {
        return "/api/cart/items/" + productId;
    }
}
