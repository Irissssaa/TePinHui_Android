package com.example.tepinhui.ui.community;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.R;

public class PostPublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_publish);

        findViewById(R.id.btn_publish).setOnClickListener(v -> {
            Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
            finish(); // 返回社区
        });
    }
}
