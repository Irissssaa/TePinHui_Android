package com.example.tepinhui.ui.community;

public class CommunityApi {

    // ==================== 帖子列表 ====================

    // source: HELP / HOT / RECOMMEND
    public static String posts(String source, int page, int size) {
        return "/api/community/posts"
                + "?source=" + source
                + "&page=" + page
                + "&size=" + size;
    }

    // ==================== 帖子详情 ====================

    public static String postDetail(int postId) {
        return "/api/community/posts/" + postId;
    }

    // ==================== 评论列表 ====================

    public static String comments(int postId) {
        return "/api/community/posts/" + postId + "/comments";
    }

    // ==================== 发帖（你现在缺的就是这个） ====================

    public static String publishPost() {
        return "/api/community/posts";
    }

    // ==================== 发评论（下一步会用） ====================

    public static String publishComment(int postId) {
        return "/api/community/posts/" + postId + "/comments";
    }
}
