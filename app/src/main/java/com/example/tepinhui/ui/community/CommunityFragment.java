package com.example.tepinhui.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tepinhui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {

    private TabLayout tabLayout;

    private static int defaultTabIndex = 0;

    public static void setDefaultTab(int index) {
        if (index >= 0) {
            defaultTabIndex = index;
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);

        FloatingActionButton fabPost = view.findViewById(R.id.fab_post);
        fabPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostPublishActivity.class);
            int pos = tabLayout.getSelectedTabPosition();
            // 默认互助区；也允许在热点/安利里发帖（按后端 source 校验）
            String source = "HELP";
            if (pos == 0) source = "HOT";
            else if (pos == 1) source = "RECOMMEND";
            intent.putExtra(PostPublishActivity.EXTRA_SOURCE, source);
            startActivity(intent);
        });

        // 添加 Tab
        tabLayout.addTab(tabLayout.newTab().setText("本周热点"));
        tabLayout.addTab(tabLayout.newTab().setText("好物安利"));
        tabLayout.addTab(tabLayout.newTab().setText("互助区"));

        // 默认选中指定 Tab
        TabLayout.Tab tab = tabLayout.getTabAt(defaultTabIndex);
        if (tab != null) {
            tab.select();
        }

        // 同步 Fragment
        switch (defaultTabIndex) {
            case 0:
                replaceFragment(new HotFragment());
                break;
            case 1:
                replaceFragment(new RecommendFragment());
                break;
            case 2:
                replaceFragment(new HelpFragment());
                break;
        }


        // Tab 切换逻辑
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment target;
                switch (tab.getPosition()) {
                    case 0:
                        target = new HotFragment();
                        break;
                    case 1:
                        target = new RecommendFragment();
                        break;
                    case 2:
                        target = new HelpFragment();
                        break;
                    default:
                        return;
                }
                replaceFragment(target);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.community_container, fragment)
                .commit();
    }
}
