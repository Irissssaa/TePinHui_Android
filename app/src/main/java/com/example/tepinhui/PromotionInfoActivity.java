package com.example.tepinhui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PromotionInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_info);

        // 设置返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        // 设置管理按钮
        View tvManage = findViewById(R.id.tv_manage);
        if (tvManage != null) {
            tvManage.setOnClickListener(v -> {
                // 管理活动逻辑
                Toast.makeText(this, "管理活动", Toast.LENGTH_SHORT).show();
            });
        }

        // 设置筛选标签点击事件
        setupFilterTabs();
    }

    private void setupFilterTabs() {
        TextView tabAll = findViewById(R.id.tab_all);
        TextView tabOngoing = findViewById(R.id.tab_ongoing);
        TextView tabExpired = findViewById(R.id.tab_expired);

        if (tabAll != null) {
            tabAll.setOnClickListener(v -> {
                // 使用 ContextCompat 获取颜色资源（推荐方式）
                tabAll.setTextColor(ContextCompat.getColor(this, R.color.red_primary));

                if (tabOngoing != null)
                    tabOngoing.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                if (tabExpired != null)
                    tabExpired.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                Toast.makeText(this, "显示全部活动", Toast.LENGTH_SHORT).show();
            });
        }

        if (tabOngoing != null) {
            tabOngoing.setOnClickListener(v -> {
                tabOngoing.setTextColor(ContextCompat.getColor(this, R.color.red_primary));

                if (tabAll != null)
                    tabAll.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                if (tabExpired != null)
                    tabExpired.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                Toast.makeText(this, "显示进行中活动", Toast.LENGTH_SHORT).show();
            });
        }

        if (tabExpired != null) {
            tabExpired.setOnClickListener(v -> {
                tabExpired.setTextColor(ContextCompat.getColor(this, R.color.red_primary));

                if (tabAll != null)
                    tabAll.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                if (tabOngoing != null)
                    tabOngoing.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));

                Toast.makeText(this, "显示已过期活动", Toast.LENGTH_SHORT).show();
            });
        }
    }
}