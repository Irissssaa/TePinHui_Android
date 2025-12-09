package com.example.tepinhui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LikeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_info);

        // 设置返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        // 设置管理按钮
        View tvManage = findViewById(R.id.tv_manage);
        if (tvManage != null) {
            tvManage.setOnClickListener(v -> {
                // 管理点赞消息逻辑
            });
        }
    }
}