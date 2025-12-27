package com.example.tepinhui.dto;

public class ServiceCategoryDTO {
    private String name;
    private String description;
    private String iconUrl;

    // 构造函数
    public ServiceCategoryDTO(String name, String description, String iconUrl) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    // Getter和Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}