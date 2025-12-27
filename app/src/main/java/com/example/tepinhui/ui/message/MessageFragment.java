package com.example.tepinhui.ui.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tepinhui.PromotionInfoActivity;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.MessageDTO;
import com.example.tepinhui.network.MessageApiService;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.ui.mine.LikeInfoActivity;
import com.example.tepinhui.ui.order.LogisticsInfoActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";

    // UI组件
    private TextView tabLogistics, tabPromotion, tabLike;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View layoutEmpty;
    private TextView tvToolbarTitle;

    // 适配器和数据
    private MessageAdapter adapter;
    private List<MessageDTO> messageList = new ArrayList<>();

    // 当前消息类型: logistics, promotion, like, all
    private String currentType = "all";
    private int currentPage = 1;
    private final int pageSize = 20;

    // 未读消息数量
    private Map<String, Integer> unreadCounts = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图
        initViews(view);

        // 初始化RecyclerView和适配器
        initRecyclerView();

        // 初始化下拉刷新
        initSwipeRefresh();

        // 初始化点击监听器
        initClickListeners(view);

        // 设置默认选中的标签（物流信息）
        selectTab(0);

        // 加载未读消息数量
        loadUnreadCount();

        // 加载消息列表
        loadMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新可见时刷新消息列表
        refreshMessages();
    }

    private void initViews(View view) {
        try {
            // 标签
            tabLogistics = view.findViewById(R.id.tab_logistics);
            tabPromotion = view.findViewById(R.id.tab_promotion);
            tabLike = view.findViewById(R.id.tab_like);

            // RecyclerView
            recyclerView = view.findViewById(R.id.rv_messages);

            // 下拉刷新
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            if (swipeRefreshLayout == null) {
                // 如果XML中没有SwipeRefreshLayout，创建一个
                swipeRefreshLayout = new SwipeRefreshLayout(requireContext());
            }

            // 空状态
            layoutEmpty = view.findViewById(R.id.layout_empty);

            // 标题
            tvToolbarTitle = view.findViewById(R.id.tv_toolbar_title);
            if (tvToolbarTitle == null) {
                View toolbar = view.findViewById(R.id.layout_toolbar);
                if (toolbar != null) {
                    tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化视图时出错: " + e.getMessage());
        }
    }

    private void initRecyclerView() {
        adapter = new MessageAdapter(requireContext(), messageList);

        // 设置点击监听器
        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MessageDTO message) {
                handleMessageClick(message);
            }

            @Override
            public void onLongClick(MessageDTO message, int position) {
                showMessageOptions(message, position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeRefresh() {
        // 检查SwipeRefreshLayout是否已添加到布局
        if (swipeRefreshLayout.getParent() == null && getView() != null) {
            // 添加到布局中
            ViewGroup parent = (ViewGroup) recyclerView.getParent();
            int index = parent.indexOfChild(recyclerView);
            parent.removeView(recyclerView);
            swipeRefreshLayout.addView(recyclerView);
            parent.addView(swipeRefreshLayout, index);
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            loadMessages();
        });
    }

    @SuppressLint("CutPasteId")
    private void initClickListeners(View view) {
        try {
            // 1. 设置按钮
            View ivSetting = view.findViewById(R.id.iv_setting);
            if (ivSetting != null) {
                ivSetting.setOnClickListener(v -> {
                    Log.d(TAG, "点击：设置");
                    Toast.makeText(getContext(), "跳转到消息设置", Toast.LENGTH_SHORT).show();
                    navigateToActivity(MessageSettingActivity.class);
                });
            }

            // 2. 搜索框
            View layoutSearch = view.findViewById(R.id.layout_search);
            if (layoutSearch != null) {
                layoutSearch.setOnClickListener(v -> {
                    Log.d(TAG, "点击：搜索");
                    Toast.makeText(getContext(), "跳转到消息搜索", Toast.LENGTH_SHORT).show();
                    navigateToActivity(MessageSearchActivity.class);
                });
            }

            // 3. 全部已读
            View tvAllRead = view.findViewById(R.id.tv_all_read);
            if (tvAllRead != null) {
                tvAllRead.setOnClickListener(v -> {
                    Log.d(TAG, "点击：全部已读");
                    markAllAsRead();
                });
            }

            // 4. 物流信息标签
            if (tabLogistics != null) {
                tabLogistics.setOnClickListener(v -> {
                    Log.d(TAG, "点击：物流信息标签");
                    selectTab(0);
                    currentType = "order"; // 对应物流信息
                    refreshMessages();
                });
            }

            // 5. 活动优惠标签
            if (tabPromotion != null) {
                tabPromotion.setOnClickListener(v -> {
                    Log.d(TAG, "点击：活动优惠标签");
                    selectTab(1);
                    currentType = "promotion"; // 对应活动优惠
                    refreshMessages();
                });
            }

            // 6. 点赞消息标签
            if (tabLike != null) {
                tabLike.setOnClickListener(v -> {
                    Log.d(TAG, "点击：点赞消息标签");
                    selectTab(2);
                    currentType = "like"; // 对应点赞消息
                    refreshMessages();
                });
            }

        } catch (Exception e) {
            Log.e(TAG, "初始化点击监听器时出错: " + e.getMessage());
            Toast.makeText(getContext(), "功能初始化异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectTab(int position) {
        // 重置所有标签颜色
        resetTabColors();

        // 设置选中的标签颜色
        if (getContext() != null) {
            int selectedColor = ContextCompat.getColor(getContext(), R.color.red_primary);
            int normalColor = ContextCompat.getColor(getContext(), R.color.gray_dark);

            switch (position) {
                case 0: // 物流信息
                    if (tabLogistics != null) {
                        tabLogistics.setTextColor(selectedColor);
                        // 更新标签显示，包含未读数量
                        updateTabText(tabLogistics, "物流信息", unreadCounts.get("logistics"));
                    }
                    break;
                case 1: // 活动优惠
                    if (tabPromotion != null) {
                        tabPromotion.setTextColor(selectedColor);
                        updateTabText(tabPromotion, "活动优惠", unreadCounts.get("promotion"));
                    }
                    break;
                case 2: // 点赞消息
                    if (tabLike != null) {
                        tabLike.setTextColor(selectedColor);
                        updateTabText(tabLike, "点赞消息", unreadCounts.get("like"));
                    }
                    break;
            }
        }
    }

    private void updateTabText(TextView tab, String baseText, Integer unreadCount) {
        if (unreadCount != null && unreadCount > 0) {
            tab.setText(baseText + "(" + unreadCount + ")");
        } else {
            tab.setText(baseText);
        }
    }

    private void resetTabColors() {
        if (getContext() != null) {
            int normalColor = ContextCompat.getColor(getContext(), R.color.gray_dark);

            if (tabLogistics != null) {
                tabLogistics.setTextColor(normalColor);
                updateTabText(tabLogistics, "物流信息", unreadCounts.get("logistics"));
            }
            if (tabPromotion != null) {
                tabPromotion.setTextColor(normalColor);
                updateTabText(tabPromotion, "活动优惠", unreadCounts.get("promotion"));
            }
            if (tabLike != null) {
                tabLike.setTextColor(normalColor);
                updateTabText(tabLike, "点赞消息", unreadCounts.get("like"));
            }
        }
    }

    private void loadMessages() {
        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();

        MessageApiService.getMessages(getContext(), currentPage, pageSize, new NetworkUtils.Callback<Result<Map<String, Object>>>() {
            @Override
            public void onSuccess(Result<Map<String, Object>> result) {
                swipeRefreshLayout.setRefreshing(false);

                if (result != null && result.isSuccess()) {
                    Map<String, Object> data = result.getData();
                    if (data != null && data.containsKey("list")) {
                        List<Map<String, Object>> rawList = (List<Map<String, Object>>) data.get("list");
                        List<MessageDTO> newMessages = convertToMessageDTOList(rawList);

                        // 筛选消息类型
                        List<MessageDTO> filteredMessages = filterMessagesByType(newMessages, currentType);

                        if (currentPage == 1) {
                            messageList.clear();
                        }
                        messageList.addAll(filteredMessages);
                        adapter.updateData(messageList);

                        // 更新UI
                        updateUI();

                        // 重新加载未读数量
                        loadUnreadCount();
                    }
                } else {
                    Toast.makeText(getContext(),
                            result != null ? result.getMsg() : "加载失败",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private List<MessageDTO> convertToMessageDTOList(List<Map<String, Object>> rawList) {
        List<MessageDTO> messages = new ArrayList<>();

        for (Map<String, Object> map : rawList) {
            MessageDTO message = new MessageDTO();

            try {
                // 转换ID
                if (map.containsKey("id")) {
                    Object idObj = map.get("id");
                    if (idObj instanceof Number) {
                        message.setId(((Number) idObj).intValue());
                    }
                }

                // 转换标题
                if (map.containsKey("title")) {
                    message.setTitle((String) map.get("title"));
                }

                // 转换内容
                if (map.containsKey("content")) {
                    message.setContent((String) map.get("content"));
                }

                // 转换类型
                if (map.containsKey("type")) {
                    message.setType((String) map.get("type"));
                }

                // 转换已读状态
                if (map.containsKey("isRead")) {
                    Object readObj = map.get("isRead");
                    if (readObj instanceof Boolean) {
                        message.setIsRead((Boolean) readObj);
                    } else if (readObj instanceof Number) {
                        message.setIsRead(((Number) readObj).intValue() == 1);
                    }
                }

                // 转换创建时间
                if (map.containsKey("createdAt")) {
                    // 这里需要根据实际情况转换时间格式
                    // 假设后端返回的是时间戳或字符串
                }

                messages.add(message);
            } catch (Exception e) {
                Log.e(TAG, "转换消息数据时出错: " + e.getMessage());
            }
        }

        return messages;
    }

    private List<MessageDTO> filterMessagesByType(List<MessageDTO> messages, String type) {
        if ("all".equals(type)) {
            return messages;
        }

        List<MessageDTO> filtered = new ArrayList<>();
        for (MessageDTO message : messages) {
            if (type.equals(message.getType())) {
                filtered.add(message);
            }
        }
        return filtered;
    }

    private void loadUnreadCount() {
        MessageApiService.getUnreadCount(getContext(), new NetworkUtils.Callback<Result<Map<String, Integer>>>() {
            @Override
            public void onSuccess(Result<Map<String, Integer>> result) {
                if (result != null && result.isSuccess()) {
                    unreadCounts = result.getData();
                    if (unreadCounts != null) {
                        // 更新标签显示
                        updateTabText(tabLogistics, "物流信息", unreadCounts.get("logistics"));
                        updateTabText(tabPromotion, "活动优惠", unreadCounts.get("promotion"));
                        updateTabText(tabLike, "点赞消息", unreadCounts.get("like"));
                    }
                }
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, "加载未读消息数量失败: " + msg);
            }
        });
    }

    private void updateUI() {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (messageList.isEmpty()) {
                if (layoutEmpty != null) {
                    layoutEmpty.setVisibility(View.VISIBLE);
                }
                if (recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                }
            } else {
                if (layoutEmpty != null) {
                    layoutEmpty.setVisibility(View.GONE);
                }
                if (recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void handleMessageClick(MessageDTO message) {
        // 标记为已读
        markAsRead(message.getId());

        // 根据消息类型跳转到不同页面
        String type = message.getType();
        if (type == null) {
            return;
        }

        switch (type) {
            case "order":
                // 跳转到物流信息页面
                navigateToActivity(LogisticsInfoActivity.class);
                break;
            case "promotion":
                // 跳转到活动优惠页面
                navigateToActivity(PromotionInfoActivity.class);
                break;
            case "like":
                // 跳转到点赞消息页面
                navigateToActivity(LikeInfoActivity.class);
                break;
            default:
                Toast.makeText(getContext(), "查看消息: " + message.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void markAsRead(int messageId) {
        MessageApiService.markAsRead(getContext(), messageId, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                if (result != null && result.isSuccess()) {
                    // 更新本地消息状态
                    for (MessageDTO message : messageList) {
                        if (message.getId() != null && message.getId() == messageId) {
                            message.setIsRead(true);
                            break;
                        }
                    }
                    adapter.updateData(messageList);

                    // 重新加载未读数量
                    loadUnreadCount();
                }
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, "标记已读失败: " + msg);
            }
        });
    }

    private void markAllAsRead() {
        // 收集所有未读消息的ID
        List<Integer> unreadIds = new ArrayList<>();
        for (MessageDTO message : messageList) {
            if (message.getIsRead() != null && !message.getIsRead()) {
                unreadIds.add(message.getId());
            }
        }

        if (unreadIds.isEmpty()) {
            Toast.makeText(getContext(), "没有未读消息", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageApiService.batchMarkAsRead(getContext(), unreadIds, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                Toast.makeText(getContext(), "全部标记为已读", Toast.LENGTH_SHORT).show();

                // 更新本地所有消息为已读
                for (MessageDTO message : messageList) {
                    message.setIsRead(true);
                }
                adapter.updateData(messageList);

                // 重新加载未读数量
                loadUnreadCount();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), "标记失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessageOptions(MessageDTO message, int position) {
        // 显示删除等操作的对话框
        Toast.makeText(getContext(), "长按消息: " + message.getTitle(), Toast.LENGTH_SHORT).show();
        // 可以在这里实现删除等功能
    }

    private void refreshMessages() {
        currentPage = 1;
        loadMessages();
    }

    private void navigateToActivity(Class<?> activityClass) {
        try {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), activityClass);
                startActivity(intent);
            } else {
                Log.e(TAG, "getActivity() 返回 null");
                Toast.makeText(getContext(), "跳转失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "跳转失败: " + e.getMessage());

            if (e instanceof android.content.ActivityNotFoundException) {
                Toast.makeText(getContext(), "目标页面不存在，请检查Activity是否已创建", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}