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

import com.example.tepinhui.PromotionInfoActivity;
import com.example.tepinhui.R;
import com.example.tepinhui.ui.mine.LikeInfoActivity;
import com.example.tepinhui.ui.order.LogisticsInfoActivity;

public class MessageFragment extends Fragment {

    private TextView tabLogistics, tabPromotion, tabLike;

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

        // 初始化点击监听器
        initClickListeners(view);

        // 设置默认选中的标签（物流信息）
        selectTab(0);
    }

    private void initViews(View view) {
        tabLogistics = view.findViewById(R.id.tab_logistics);
        tabPromotion = view.findViewById(R.id.tab_promotion);
        tabLike = view.findViewById(R.id.tab_like);
    }

    private void initClickListeners(View view) {
        try {
            // 1. 设置按钮
            View ivSetting = view.findViewById(R.id.iv_setting);
            if (ivSetting != null) {
                ivSetting.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：设置");
                    Toast.makeText(getContext(), "跳转到消息设置", Toast.LENGTH_SHORT).show();
                    navigateToActivity(MessageSettingActivity.class);
                });
            }

            // 2. 搜索框
            View layoutSearch = view.findViewById(R.id.layout_search);
            if (layoutSearch != null) {
                layoutSearch.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：搜索");
                    Toast.makeText(getContext(), "跳转到消息搜索", Toast.LENGTH_SHORT).show();
                    navigateToActivity(MessageSearchActivity.class);
                });
            }

            // 3. 全部已读
            View tvAllRead = view.findViewById(R.id.tv_all_read);
            if (tvAllRead != null) {
                tvAllRead.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：全部已读");
                    Toast.makeText(getContext(), "全部消息已标记为已读", Toast.LENGTH_SHORT).show();
                    // 这里可以添加标记所有消息为已读的逻辑
                });
            }

            // 4. 物流信息标签
            if (tabLogistics != null) {
                tabLogistics.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：物流信息标签");
                    selectTab(0);
                    // 这里可以刷新消息列表显示物流信息
                    // 同时跳转到物流信息详细页面
                    navigateToActivity(LogisticsInfoActivity.class);
                });
            }

            // 5. 活动优惠标签
            if (tabPromotion != null) {
                tabPromotion.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：活动优惠标签");
                    selectTab(1);
                    // 这里可以刷新消息列表显示活动优惠信息
                    // 同时跳转到活动优惠详细页面
                    navigateToActivity(PromotionInfoActivity.class);
                });
            }

            // 6. 点赞消息标签
            if (tabLike != null) {
                tabLike.setOnClickListener(v -> {
                    Log.d("MessageFragment", "点击：点赞消息标签");
                    selectTab(2);
                    // 这里可以刷新消息列表显示点赞信息
                    // 同时跳转到点赞消息详细页面
                    navigateToActivity(LikeInfoActivity.class);
                });
            }

            // 7. 消息设置按钮（可选）
            @SuppressLint("CutPasteId") View ivSetting2 = view.findViewById(R.id.iv_setting);
            if (ivSetting2 != null) {
                ivSetting2.setOnClickListener(v -> navigateToActivity(MessageSettingActivity.class));
            }

        } catch (Exception e) {
            Log.e("MessageFragment", "初始化点击监听器时出错: " + e.getMessage());
            Toast.makeText(getContext(), "功能初始化异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectTab(int position) {
        // 重置所有标签颜色
        resetTabColors();

        // 设置选中的标签颜色
        if (getContext() != null) {
            int selectedColor = ContextCompat.getColor(getContext(), R.color.red_primary);

            switch (position) {
                case 0: // 物流信息
                    if (tabLogistics != null) tabLogistics.setTextColor(selectedColor);
                    break;
                case 1: // 活动优惠
                    if (tabPromotion != null) tabPromotion.setTextColor(selectedColor);
                    break;
                case 2: // 点赞消息
                    if (tabLike != null) tabLike.setTextColor(selectedColor);
                    break;
            }
        }
    }

    private void resetTabColors() {
        if (getContext() != null) {
            int normalColor = ContextCompat.getColor(getContext(), R.color.gray_dark);

            if (tabLogistics != null) tabLogistics.setTextColor(normalColor);
            if (tabPromotion != null) tabPromotion.setTextColor(normalColor);
            if (tabLike != null) tabLike.setTextColor(normalColor);
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        try {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), activityClass);
                startActivity(intent);
            } else {
                Log.e("MessageFragment", "getActivity() 返回 null");
                Toast.makeText(getContext(), "跳转失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MessageFragment", "跳转失败: " + e.getMessage());

            if (e instanceof android.content.ActivityNotFoundException) {
                Toast.makeText(getContext(), "目标页面不存在，请检查Activity是否已创建", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}