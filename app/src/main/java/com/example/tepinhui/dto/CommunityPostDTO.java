package com.example.tepinhui.dto;

import java.util.List;

public class CommunityPostDTO {
    public int id;
    public String userName;
    public String avatarUrl;
    public String content;
    public List<String> images;
    public int likeCount;
    public int commentCount;
    public String createdAt;
    public String source;
}