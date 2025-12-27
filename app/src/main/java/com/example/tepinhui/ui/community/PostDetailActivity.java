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

import com.example.tepinhui.R;
import com.example.tepinhui.ui.community.CommunityPost;
import com.example.tepinhui.ui.community.PostHeaderAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "extra_post";

    private RecyclerView rvComments;
    private EditText etComment;
    private TextView btnSend;

    private PostHeaderAdapter headerAdapter;
    private CommentAdapter commentAdapter; // 下一步我们会实现
    private List<CommunityComment> commentList = new ArrayList<>();
    private CommunityPost post;

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

        initMockComments();   // ① 先准备数据
        commentAdapter = new CommentAdapter(commentList);
        ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, commentAdapter);
        rvComments.setAdapter(concatAdapter);

        setupSendInteraction();
    }

    private void initMockComments() {

        commentList.clear();

        commentList.add(new CommunityComment(
                "c1",
                "小张",
                0,
                "这个真的很好吃！",
                "10分钟前",
                3,
                null,
                null
        ));

        commentList.add(new CommunityComment(
                "c2",
                "小李",
                0,
                "我也觉得性价比很高",
                "8分钟前",
                1,
                "c1",
                "小张"
        ));

        commentList.add(new CommunityComment(
                "c3",
                "小王",
                0,
                "楼主在哪里买的？",
                "5分钟前",
                0,
                null,
                null
        ));
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

        // 真实产品：这里会发请求并乐观插入列表；你现在是 mock，先本地插入一条
        // 下一步我们实现 CommunityComment 后，这里改成插入 CommentAdapter
        Toast.makeText(this, "已发送（mock）：" + content, Toast.LENGTH_SHORT).show();
        etComment.setText("");
    }
}
