package com.example.tepinhui.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceCenterActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvMore, tvServiceStatus;
    private RecyclerView rvMessages;
    private EditText etInput;
    private Button btnSend;

    // 这里需要创建一个消息适配器
    // private MessageAdapter messageAdapter;
    // private List<MessageDTO> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        initViews();
        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvMore = findViewById(R.id.tv_more);
        tvServiceStatus = findViewById(R.id.tv_service_status);
        rvMessages = findViewById(R.id.rv_messages);
        etInput = findViewById(R.id.et_input);
        btnSend = findViewById(R.id.btn_send);

        // 初始化消息列表
        // messages = new ArrayList<>();
        // messageAdapter = new MessageAdapter(messages);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        // rvMessages.setAdapter(messageAdapter);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        tvMore.setOnClickListener(v -> {
            Toast.makeText(this, "更多选项", Toast.LENGTH_SHORT).show();
        });

        btnSend.setOnClickListener(v -> {
            String message = etInput.getText().toString().trim();
            if (!message.isEmpty()) {
                // 发送消息
                // 这里添加发送消息的逻辑
                etInput.setText("");
            }
        });
    }
}