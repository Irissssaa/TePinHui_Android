package com.example.tepinhui;

public class FavoriteItem {
    private String name;
    private String type;
    private double rating;
    private int imageResId;
    private String category;

    public FavoriteItem(String name, String type, double rating, int imageResId, String category) {
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.imageResId = imageResId;
        this.category = category;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public double getRating() { return rating; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
}