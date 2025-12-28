package com.example.tepinhui.ui.community;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.CommunityCommentDTO;
import com.example.tepinhui.dto.CommunityPostDTO;
import com.example.tepinhui.network.UserApiService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "extra_post";

    private RecyclerView rvComments;
    private EditText etComment;
    private TextView btnSend;

    private PostHeaderAdapter headerAdapter;
    private CommentAdapter commentAdapter; // 下一步我们会实现
    private List<CommunityComment> commentList = new ArrayList<>();
    private CommunityPost post;

    // 回复模式
    private Integer replyParentId = null;
    private String replyToUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        rvComments = findViewById(R.id.rvComments);
        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSend);

        post = (CommunityPost) getIntent().getSerializableExtra(EXTRA_POST);
        if (post == null) {
            Toast.makeText(this, "帖子数据为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        headerAdapter = new PostHeaderAdapter(post);
        headerAdapter.setOnLikeClickListener(p -> likePost());

        commentAdapter = new CommentAdapter(commentList);
        commentAdapter.setOnCommentClickListener(c -> {
            // 点击评论进入“回复模式”
            if (c == null || c.getCommentId() == null) return;
            try {
                replyParentId = Integer.parseInt(c.getCommentId());
            } catch (Exception ignored) {
                replyParentId = null;
            }
            replyToUser = c.getUserName();
            if (replyToUser != null && !replyToUser.trim().isEmpty()) {
                etComment.setHint("回复 @" + replyToUser);
            } else {
                etComment.setHint("回复");
            }
            etComment.requestFocus();
        });
        commentAdapter.setOnCommentLongClickListener((c, position) -> likeComment(c, position));
        ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, commentAdapter);
        rvComments.setAdapter(concatAdapter);

        setupSendInteraction();

        // 真实数据：拉取帖子详情（刷新 commentCount/likeCount）+ 评论列表
        loadPostDetail();
        loadComments();
    }

    private void loadPostDetail() {
        if (post == null || post.getPostId() == null) return;
        String url = CommunityApi.postDetail(post.getPostId());
        Type type = new TypeToken<Result<CommunityPostDTO>>() {}.getType();
        NetworkUtils.get(url, type, new NetworkUtils.Callback<Result<CommunityPostDTO>>() {
            @Override
            public void onSuccess(Result<CommunityPostDTO> result) {
                if (result == null || !result.isSuccess() || result.getData() == null) return;
                CommunityPostDTO dto = result.getData();
                // 用后端数据刷新 header（头像/图片暂用本地占位逻辑）
                CommunityPost updated = new CommunityPost(
                        dto.id,
                        dto.userName,
                        AvatarUtil.forUser(dto.userName),
                        dto.content,
                        dto.createdAt,
                        dto.likeCount,
                        dto.commentCount,
                        new ArrayList<>()
                );
                updated.setAvatarUrl(dto.avatarUrl);
                updated.setImageUrls(dto.images);
                post = updated;
                headerAdapter.updatePost(updated);
            }

            @Override
            public void onError(String msg) {
                // 不阻塞展示：保留传进来的 post
            }
        });
    }

    private void loadComments() {
        if (post == null || post.getPostId() == null) return;
        String url = CommunityApi.comments(post.getPostId());
        Type type = new TypeToken<Result<List<CommunityCommentDTO>>>() {}.getType();
        NetworkUtils.get(url, type, new NetworkUtils.Callback<Result<List<CommunityCommentDTO>>>() {
            @Override
            public void onSuccess(Result<List<CommunityCommentDTO>> result) {
                if (result == null || !result.isSuccess() || result.getData() == null) return;

                List<CommunityComment> newList = new ArrayList<>();
                for (CommunityCommentDTO dto : result.getData()) {
                    CommunityComment c = new CommunityComment(
                            String.valueOf(dto.id),
                            dto.userName,
                            AvatarUtil.forUser(dto.userName),
                            dto.content,
                            dto.createdAt,
                            dto.likeCount,
                            dto.parentId == null ? null : String.valueOf(dto.parentId),
                            dto.replyToUser
                    );
                    c.setAvatarUrl(dto.avatarUrl);
                    newList.add(c);
                }
                commentAdapter.replaceAll(newList);
            }

            @Override
            public void onError(String msg) {
                // 可选 Toast，但避免刷屏
            }
        });
    }

    private void likePost() {
        if (post == null || post.getPostId() == null) return;

        String token = UserApiService.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "请先登录再点赞", Toast.LENGTH_SHORT).show();
            return;
        }

        // 先乐观更新一次 UI
        try {
            post.setLikeCount(post.getLikeCount() + 1);
            headerAdapter.updatePost(post);
        } catch (Exception ignored) {
        }

        String url = "/api/community/posts/" + post.getPostId() + "/like";
        Type type = new TypeToken<Result<Object>>() {}.getType();
        NetworkUtils.post(url, null, token, type, new NetworkUtils.Callback<Result<Object>>() {
            @Override
            public void onSuccess(Result<Object> result) {
                if (result == null || !result.isSuccess()) {
                    Toast.makeText(PostDetailActivity.this, result != null ? result.getMsg() : "点赞失败", Toast.LENGTH_SHORT).show();
                    loadPostDetail(); // 纠正回后端真实值
                    return;
                }
                loadPostDetail();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(PostDetailActivity.this, "点赞失败：" + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
                loadPostDetail();
            }
        });
    }

    private void likeComment(CommunityComment c, int position) {
        if (c == null || c.getCommentId() == null) return;

        String token = UserApiService.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "请先登录再点赞", Toast.LENGTH_SHORT).show();
            return;
        }

        int commentId;
        try {
            commentId = Integer.parseInt(c.getCommentId());
        } catch (Exception e) {
            return;
        }

        // 乐观更新
        try {
            c.setLikeCount(c.getLikeCount() + 1);
            commentAdapter.notifyItemChanged(position);
        } catch (Exception ignored) {
        }

        String url = "/api/community/comments/" + commentId + "/like";
        Type type = new TypeToken<Result<Object>>() {}.getType();
        NetworkUtils.post(url, null, token, type, new NetworkUtils.Callback<Result<Object>>() {
            @Override
            public void onSuccess(Result<Object> result) {
                if (result == null || !result.isSuccess()) {
                    Toast.makeText(PostDetailActivity.this, result != null ? result.getMsg() : "点赞失败", Toast.LENGTH_SHORT).show();
                    loadComments(); // 纠正回后端真实值
                    return;
                }
                // 可选：不强刷，保持乐观值
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(PostDetailActivity.this, "点赞失败：" + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
                loadComments();
            }
        });
    }


    private void setupSendInteraction() {
        btnSend.setOnClickListener(v -> trySend());

        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                trySend();
                return true;
            }
            return false;
        });
    }

    private void trySend() {
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (post == null || post.getPostId() == null) return;

        String token = UserApiService.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "请先登录再评论", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("content", content);
        body.put("parentId", replyParentId);

        Type type = new TypeToken<Result<Object>>() {}.getType();
        String url = CommunityApi.comments(post.getPostId()); // POST /posts/{postId}/comments
        NetworkUtils.post(url, body, token, type, new NetworkUtils.Callback<Result<Object>>() {
            @Override
            public void onSuccess(Result<Object> result) {
                if (result == null || !result.isSuccess()) {
                    Toast.makeText(PostDetailActivity.this, result != null ? result.getMsg() : "发送失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                etComment.setText("");
                replyParentId = null;
                replyToUser = null;
                etComment.setHint("说点什么…");
                // 发送成功后刷新评论列表（后端会 commentCount+1）
                loadPostDetail();
                loadComments();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(PostDetailActivity.this, "发送失败：" + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
