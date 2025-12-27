package com.example.tepinhui.ui.community;

import java.io.Serializable;

public class CommunityComment implements Serializable {

    private String commentId;
    private String userName;
    private int avatarResId;
    private String content;
    private String time;
    private int likeCount;

    // 回复相关
    private String parentId;
    private String replyToUser;

    public CommunityComment(String commentId,
                            String userName,
                            int avatarResId,
                            String content,
                            String time,
                            int likeCount,
                            String parentId,
                            String replyToUser) {
        this.commentId = commentId;
        this.userName = userName;
        this.avatarResId = avatarResId;
        this.content = content;
        this.time = time;
        this.likeCount = likeCount;
        this.parentId = parentId;
        this.replyToUser = replyToUser;
    }

    public String getCommentId() { return commentId; }
    public String getUserName() { return userName; }
    public int getAvatarResId() { return avatarResId; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public int getLikeCount() { return likeCount; }
    public String getParentId() { return parentId; }
    public String getReplyToUser() { return replyToUser; }
}
