package com.example.tepinhui.dto;

public class ProductDTO {

    // ===== 基础商品信息 =====
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private String origin;
    private int sales;
    private int stock;
    private String category;

    // ===== 内容化字段 =====
    private String shortIntro;   // 简短介绍
    private String cultureNote;  // 产地 / 文化说明
    private Integer storyId;     // 关联的特产故事（可为空）

    // ===== Getter =====
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOrigin() {
        return origin;
    }

    public int getSales() {
        return sales;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public String getCultureNote() {
        return cultureNote;
    }

    public Integer getStoryId() {
        return storyId;
    }
}
