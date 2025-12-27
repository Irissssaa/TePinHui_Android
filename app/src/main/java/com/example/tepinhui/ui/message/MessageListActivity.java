package com.example.tepinhui.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.MessageDTO;
import com.example.tepinhui.network.MessageApiService;
import com.example.tepinhui.NetworkUtils;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View layoutEmpty;
    private TextView tvToolbarTitle;
    private MessageAdapter adapter;
    private List<MessageDTO> messageList = new ArrayList<>();

    private String currentType = "all"; // 当前消息类型: all, logistics, promotion, like
    private int currentPage = 1;
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        initViews();
        initToolbar();
        initRecyclerView();
        initSwipeRefresh();
        loadMessages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_messages);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        layoutEmpty = findViewById(R.id.layout_empty);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
    }

    private void initToolbar() {
        ImageView ivBack = findViewById(R.id.iv_back);
        TextView tvAllRead = findViewById(R.id.tv_all_read);
        ImageView ivSetting = findViewById(R.id.iv_setting);

        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        if (tvAllRead != null) {
            tvAllRead.setOnClickListener(v -> markAllAsRead());
        }

        if (ivSetting != null) {
            ivSetting.setOnClickListener(v -> {
                // 跳转到消息设置
                // Intent intent = new Intent(this, MessageSettingActivity.class);
                // startActivity(intent);
            });
        }
    }

    private void initRecyclerView() {
        adapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MessageDTO message) {
                // 点击消息跳转到详情
                openMessageDetail(message);
            }

            @Override
            public void onLongClick(MessageDTO message, int position) {
                // 长按显示操作菜单
                showMessageMenu(message, position);
            }
        });
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            loadMessages();
        });
    }

    private void loadMessages() {
        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();

        MessageApiService.getMessages(this, currentPage, pageSize, new NetworkUtils.Callback<Result<Map<String, Object>>>() {
            @Override
            public void onSuccess(Result<Map<String, Object>> result) {
                swipeRefreshLayout.setRefreshing(false);

                if (result != null && result.isSuccess()) {
                    Map<String, Object> data = result.getData();
                    if (data != null && data.containsKey("list")) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
                        List<MessageDTO> messages = new ArrayList<>();

                        for (Map<String, Object> map : list) {
                            MessageDTO message = convertMapToMessageDTO(map);
                            if (message != null && filterByType(message)) {
                                messages.add(message);
                            }
                        }

                        if (currentPage == 1) {
                            messageList.clear();
                        }
                        messageList.addAll(messages);
                        adapter.updateData(messageList);

                        // 更新UI
                        updateUI();
                    }
                } else {
                    Toast.makeText(MessageListActivity.this,
                            result != null ? result.getMsg() : "加载失败",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MessageListActivity.this, msg, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private boolean filterByType(MessageDTO message) {
        if ("all".equals(currentType)) {
            return true;
        }

        if (message.getType() == null) {
            return false;
        }

        switch (currentType) {
            case "logistics":
                return "order".equals(message.getType());
            case "promotion":
                return "promotion".equals(message.getType());
            case "like":
                return "like".equals(message.getType());
            default:
                return true;
        }
    }

    private MessageDTO convertMapToMessageDTO(Map<String, Object> map) {
        MessageDTO message = new MessageDTO();

        if (map.containsKey("id")) {
            message.setId(((Double) map.get("id")).intValue());
        }
        if (map.containsKey("title")) {
            message.setTitle((String) map.get("title"));
        }
        if (map.containsKey("content")) {
            message.setContent((String) map.get("content"));
        }
        if (map.containsKey("type")) {
            message.setType((String) map.get("type"));
        }
        if (map.containsKey("isRead")) {
            message.setIsRead((Boolean) map.get("isRead"));
        }
        // 其他字段根据需要转换

        return message;
    }

    private void updateUI() {
        if (messageList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void openMessageDetail(MessageDTO message) {
        // 标记为已读
        markAsRead(message.getId());

        // 根据消息类型跳转到不同的详情页面
        if ("order".equals(message.getType())) {
            // 跳转到物流信息页面
            // Intent intent = new Intent(this, LogisticsInfoActivity.class);
            // intent.putExtra("message_id", message.getId());
            // startActivity(intent);
        } else if ("promotion".equals(message.getType())) {
            // 跳转到活动优惠页面
            // Intent intent = new Intent(this, PromotionInfoActivity.class);
            // intent.putExtra("message_id", message.getId());
            // startActivity(intent);
        } else if ("like".equals(message.getType())) {
            // 跳转到点赞消息页面
            // Intent intent = new Intent(this, LikeInfoActivity.class);
            // intent.putExtra("message_id", message.getId());
            // startActivity(intent);
        }
    }

    private void markAsRead(int messageId) {
        MessageApiService.markAsRead(this, messageId, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                // 标记成功，更新本地数据
                for (MessageDTO message : messageList) {
                    if (message.getId() == messageId) {
                        message.setIsRead(true);
                        break;
                    }
                }
                adapter.updateData(messageList);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MessageListActivity.this, "标记已读失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAllAsRead() {
        List<Integer> messageIds = new ArrayList<>();
        for (MessageDTO message : messageList) {
            if (message.getIsRead() != null && !message.getIsRead()) {
                messageIds.add(message.getId());
            }
        }

        if (messageIds.isEmpty()) {
            Toast.makeText(this, "没有未读消息", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageApiService.batchMarkAsRead(this, messageIds, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                Toast.makeText(MessageListActivity.this, "全部标记为已读", Toast.LENGTH_SHORT).show();
                // 更新本地数据
                for (MessageDTO message : messageList) {
                    message.setIsRead(true);
                }
                adapter.updateData(messageList);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MessageListActivity.this, "标记失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessageMenu(MessageDTO message, int position) {
        // 显示删除、标记等操作菜单
        // 可以使用PopupMenu或Dialog
        Toast.makeText(this, "长按消息：" + message.getTitle(), Toast.LENGTH_SHORT).show();
    }
}