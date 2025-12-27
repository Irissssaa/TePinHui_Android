package com.example.tepinhui.dto;

import java.util.Date;
import java.util.Map;

/**
 * 消息DTO
 */
public class MessageDTO {
    private Integer id;
    private String title;
    private String content;
    private String type; // 消息类型：system(系统), order(订单), promotion(促销), etc.
    private Boolean isRead;
    private Date createdAt;
    private Map<String, Object> extras; // 额外数据

    // Getter和Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> getExtras() { return extras; }
    public void setExtras(Map<String, Object> extras) { this.extras = extras; }
}