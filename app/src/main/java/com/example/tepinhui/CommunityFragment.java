package com.example.tepinhui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {

    private TabLayout tabLayout;
    private TextView tvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        tvContent = view.findViewById(R.id.tvCommunityContent);

        // 添加3个标签
        tabLayout.addTab(tabLayout.newTab().setText("本周热点"));
        tabLayout.addTab(tabLayout.newTab().setText("好物安利"));
        tabLayout.addTab(tabLayout.newTab().setText("互助区"));

        // 设置默认显示
        updateContent(0);

        // 监听标签切换
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateContent(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void updateContent(int position) {
        // 在实际开发中，这里可以切换不同的 Fragment 或 RecyclerView 数据
        switch (position) {
            case 0:
                tvContent.setText("【本周热点】\n\n1. 云南松茸上市啦！\n2. 阳澄湖大闸蟹预售开启\n3. 中秋特产礼盒排行榜");
                break;
            case 1:
                tvContent.setText("【好物安利】\n\n用户 @吃货小王 推荐了：\n正宗哈尔滨红肠，蒜香浓郁，下酒神器！");
                break;
            case 2:
                tvContent.setText("【互助区】\n\n求助：我想买正宗的宣威火腿，哪家店靠谱？\n\n回答：推荐老李家特产店...");
                break;
        }
    }
}

