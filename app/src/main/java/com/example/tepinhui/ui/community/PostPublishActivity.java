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

    private EditText etContent;

    // 示例：发帖来源（你可以从 Intent 传）
    private String source = "HELP"; // HELP / HOT / RECOMMEND

    // 示例：登录 token（你项目里已有）
    private String token = "your_token_here";

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

        // 1️⃣ 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("source", source);
        body.put("content", content);
        body.put("images", new ArrayList<>()); // 先不传图

        // 2️⃣ 调接口
        NetworkUtils.post(
                CommunityApi.publishPost(),
                body,
                token,
                Result.class,
                new NetworkUtils.Callback<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if ("0".equals(result.getCode())) {
                            Toast.makeText(PostPublishActivity.this,
                                    "发布成功", Toast.LENGTH_SHORT).show();
                            finish(); // 发完就退出
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
