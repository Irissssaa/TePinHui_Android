package com.example.tepinhui;

public class FootprintItem {
    private String name;
    private String viewTime;
    private double price;
    private int imageResId;

    public FootprintItem(String name, String viewTime, double price, int imageResId) {
        this.name = name;
        this.viewTime = viewTime;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getViewTime() { return viewTime; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}