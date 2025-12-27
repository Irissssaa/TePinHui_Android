package com.example.tepinhui.ui.community;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.CommunityCommentDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HelpDetailActivity extends AppCompatActivity {

    private RecyclerView rvAnswers;
    private PostHeaderAdapter headerAdapter;
    private CommentAdapter commentAdapter;

    private CommunityPost questionPost;
    private final List<CommunityComment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        // 1) 取帖子
        questionPost = (CommunityPost) getIntent().getSerializableExtra("extra_post");

        // 2) RecyclerView
        rvAnswers = findViewById(R.id.rvAnswers);
        rvAnswers.setLayoutManager(new LinearLayoutManager(this));

        // 3) Header
        headerAdapter = new PostHeaderAdapter(questionPost);

        // 4) 评论列表
        commentAdapter = new CommentAdapter(commentList);

        // 5) 拼接
        ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, commentAdapter);
        rvAnswers.setAdapter(concatAdapter);

        // 6) 拉评论
        loadComments();
    }

    private void loadComments() {
        if (questionPost == null || questionPost.getPostId() == null) return;

        String url = CommunityApi.comments(questionPost.getPostId());

        Type type = new TypeToken<Result<List<CommunityCommentDTO>>>() {}.getType();

        NetworkUtils.get(url, type, new NetworkUtils.Callback<Result<List<CommunityCommentDTO>>>() {
            @Override
            public void onSuccess(Result<List<CommunityCommentDTO>> result) {
                if (result == null || result.getData() == null) return;

                List<CommunityComment> newList = new ArrayList<>();

                for (CommunityCommentDTO dto : result.getData()) {
                    CommunityComment c = new CommunityComment(
                            String.valueOf(dto.id),
                            dto.userName,
                            R.drawable.avatar_1,     // 先用占位头像
                            dto.content,
                            dto.createdAt,
                            dto.likeCount,
                            dto.parentId == null ? null : String.valueOf(dto.parentId),
                            dto.replyToUser
                    );
                    newList.add(c);
                }

                commentAdapter.replaceAll(newList);
            }

            @Override
            public void onError(String msg) {
                Log.e("HelpDetailActivity", "loadComments error: " + msg);
            }
        });
    }
}
