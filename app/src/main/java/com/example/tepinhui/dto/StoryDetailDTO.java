package com.example.tepinhui.dto;

import java.util.List;

public class StoryDetailDTO {
    private int id;
    private String title;
    private String coverUrl;
    private String content;
    private List<ProductDTO> products;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCoverUrl() { return coverUrl; }
    public String getContent() { return content; }
    public List<ProductDTO> getProducts() { return products; }
}
