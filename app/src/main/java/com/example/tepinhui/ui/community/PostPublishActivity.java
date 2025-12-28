package com.example.tepinhui.ui.community;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostPublishActivity extends AppCompatActivity {

    // ✅【关键】给 CommunityFragment 用的常量
    public static final String EXTRA_SOURCE = "extra_source";

    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_publish);

        etContent = findViewById(R.id.et_content);

        findViewById(R.id.btn_publish).setOnClickListener(v -> publishPost());
    }

    private void publishPost() {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1️⃣ 从 Intent 里取 source（HOT / RECOMMEND / HELP）
        String source = getIntent().getStringExtra(EXTRA_SOURCE);
        if (TextUtils.isEmpty(source)) {
            source = "HELP"; // 兜底
        }

        // 2️⃣ token（你项目里已有登录逻辑）
        String token = getSharedPreferences("user", MODE_PRIVATE)
                .getString("token", null);

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3️⃣ 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("source", source);
        body.put("content", content);
        body.put("images", new ArrayList<>()); // 当前版本不传图

        // 4️⃣ 发请求
        NetworkUtils.post(
                "/api/community/posts",
                body,
                token,
                Result.class,
                new NetworkUtils.Callback<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if ("0".equals(result.getCode())) {
                            Toast.makeText(PostPublishActivity.this,
                                    "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PostPublishActivity.this,
                                    result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(PostPublishActivity.this,
                                msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
