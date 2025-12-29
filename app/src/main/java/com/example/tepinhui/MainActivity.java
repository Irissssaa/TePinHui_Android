package com.example.tepinhui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tepinhui.ui.cart.CartFragment;
import com.example.tepinhui.ui.community.CommunityFragment;
import com.example.tepinhui.ui.home.HomeFragment;
import com.example.tepinhui.ui.message.MessageFragment;
import com.example.tepinhui.ui.mine.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);

        // 设置默认显示首页
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // 设置监听器来处理导航项点击事件
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // 根据点击的 ID 切换对应的 Fragment
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_community) {
                selectedFragment = new CommunityFragment();
            } else if (itemId == R.id.navigation_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.navigation_message) {
                selectedFragment = new MessageFragment();
            } else if (itemId == R.id.navigation_mine) {
                selectedFragment = new MineFragment();
            }

            // 切换 Fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment) // R.id.fragment_container 是您的 FrameLayout ID
                        .commit();
                return true;
            }
            return false;
        });

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null || navView == null) return;

        String targetTab = intent.getStringExtra("target_tab");
        int communityTab = intent.getIntExtra("community_tab", -1);

        if ("community".equals(targetTab)) {
            // 先设置默认 Tab，再切换到底部导航，确保任何情况下都能生效
            CommunityFragment.setDefaultTab(communityTab);
            navView.setSelectedItemId(R.id.navigation_community);

            // 防止旋转/复用时重复触发
            intent.removeExtra("target_tab");
            intent.removeExtra("community_tab");
        }
    }
}
