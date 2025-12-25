package com.example.tepinhui.ui.message;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.tepinhui.R;

public class MessageSettingActivity extends AppCompatActivity {

    private SwitchCompat switchNotification, switchSound, switchVibrate, switchNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        // 开关控件
        switchNotification = findViewById(R.id.switch_notification);
        switchSound = findViewById(R.id.switch_sound);
        switchVibrate = findViewById(R.id.switch_vibrate);
        switchNightMode = findViewById(R.id.switch_night_mode);

        // 消息保留时间
        TextView tvRetentionTime = findViewById(R.id.tv_retention_time);
        if (tvRetentionTime != null) {
            tvRetentionTime.setOnClickListener(v -> {
                Toast.makeText(this, "设置消息保留时间", Toast.LENGTH_SHORT).show();
                // 这里可以跳转到设置消息保留时间的页面
            });
        }

        // 清空消息记录
        TextView tvClearMessages = findViewById(R.id.tv_clear_messages);
        if (tvClearMessages != null) {
            tvClearMessages.setOnClickListener(v -> {
                Toast.makeText(this, "清空消息记录", Toast.LENGTH_SHORT).show();
                // 这里可以添加清空消息记录的逻辑
            });
        }

        // 关于消息
        TextView tvAboutMessage = findViewById(R.id.tv_about_message);
        if (tvAboutMessage != null) {
            tvAboutMessage.setOnClickListener(v -> {
                Toast.makeText(this, "关于消息", Toast.LENGTH_SHORT).show();
                // 这里可以跳转到关于消息的页面
            });
        }
    }

    private void setupListeners() {
        // 开关状态改变监听
        if (switchNotification != null) {
            switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(this, isChecked ? "接收新消息通知已打开" : "接收新消息通知已关闭", Toast.LENGTH_SHORT).show());
        }

        if (switchSound != null) {
            switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(this, isChecked ? "声音已打开" : "声音已关闭", Toast.LENGTH_SHORT).show());
        }

        if (switchVibrate != null) {
            switchVibrate.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(this, isChecked ? "振动已打开" : "振动已关闭", Toast.LENGTH_SHORT).show());
        }

        if (switchNightMode != null) {
            switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(this, isChecked ? "夜间免打扰已打开" : "夜间免打扰已关闭", Toast.LENGTH_SHORT).show());
        }
    }
}