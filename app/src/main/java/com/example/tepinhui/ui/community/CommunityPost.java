package com.example.tepinhui.ui.community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommunityPost implements Serializable {

    // 基础信息
    private String userName;
    private int avatarResId;          // 本地头像资源 id（先用 drawable，后续可换 url）
    private String avatarUrl;         // 后端头像 URL（有则优先展示）
    private String content;
    private String time;
    private Integer postId;

    // 互动数据
    private int likeCount;
    private int commentCount;
    private boolean liked;            // 是否已点赞（本地假状态）

    // 帖子图片（本地 drawable 资源 id 列表；后续也可换成 url 列表）
    private List<Integer> imageList;
    private List<String> imageUrls;   // 后端图片 URL 列表（有则优先展示）

    public CommunityPost(String userName,
                         int avatarResId,
                         String content,
                         String time,
                         int likeCount,
                         int commentCount,
                         List<Integer> imageList) {
        this.userName = userName;
        this.avatarResId = avatarResId;
        this.avatarUrl = null;
        this.content = content;
        this.time = time;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.imageList = (imageList == null) ? new ArrayList<>() : imageList;
        this.imageUrls = new ArrayList<>();
        this.liked = false;
    }

    // ===== 最终构造器（推荐后面都用这个）=====
    public CommunityPost(Integer postId,
                         String userName,
                         int avatarResId,
                         String content,
                         String time,
                         int likeCount,
                         int commentCount,
                         List<Integer> imageList) {
        this.postId = postId;
        this.userName = userName;
        this.avatarResId = avatarResId;
        this.avatarUrl = null;
        this.content = content;
        this.time = time;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.imageList = (imageList == null) ? new ArrayList<>() : imageList;
        this.imageUrls = new ArrayList<>();
        this.liked = false;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = (imageUrls == null) ? new ArrayList<>() : imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
    // ===== 兼容构造器：兼容你当前的 5 参数写法（不带头像和图片）=====
    // 这样你现在的三个 Fragment 不改也能先跑起来，然后逐步补头像/图片即可。
    public CommunityPost(String userName,
                         String content,
                         String time,
                         int likeCount,
                         int commentCount) {
        this(userName,
                0, // avatarResId 默认 0，adapter 里用占位图
                content,
                time,
                likeCount,
                commentCount,
                new ArrayList<>());
    }

    // ===== Getter / Setter =====
    public String getUserName() { return userName; }

    public int getAvatarResId() { return avatarResId; }

    public String getContent() { return content; }

    public String getTime() { return time; }

    public Integer getPostId() {
        return postId;
    }
    public int getLikeCount() { return likeCount; }

    public int getCommentCount() { return commentCount; }

    public boolean isLiked() { return liked; }

    public List<Integer> getImageList() { return imageList; }

    public void setLiked(boolean liked) { this.liked = liked; }

    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
