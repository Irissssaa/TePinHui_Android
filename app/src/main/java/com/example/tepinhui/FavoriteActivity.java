package com.example.tepinhui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private TextView tabAll, tabProducts, tabShops;
    private TextView tvEdit;
    private RecyclerView rvFavorites;
    private LinearLayout layoutEmpty;
    private FavoriteAdapter adapter;
    private List<FavoriteItem> favoriteList;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initViews();
        setupListeners();
        initData();
        setupRecyclerView();

        // 默认选中全部
        selectTab(0);
        loadDataByCategory("all");
    }

    private void initViews() {
        tabAll = findViewById(R.id.tab_all);
        tabProducts = findViewById(R.id.tab_products);
        tabShops = findViewById(R.id.tab_shops);
        tvEdit = findViewById(R.id.tv_edit);
        rvFavorites = findViewById(R.id.rv_favorites);
        layoutEmpty = findViewById(R.id.layout_empty);

        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        // 编辑按钮点击
        tvEdit.setOnClickListener(v -> toggleEditMode());

        // 分类标签点击
        tabAll.setOnClickListener(v -> {
            selectTab(0);
            loadDataByCategory("all");
        });

        tabProducts.setOnClickListener(v -> {
            selectTab(1);
            loadDataByCategory("products");
        });

        tabShops.setOnClickListener(v -> {
            selectTab(2);
            loadDataByCategory("shops");
        });
    }

    private void selectTab(int position) {
        // 重置所有标签颜色
        resetTabColors();

        // 设置选中标签颜色
        int selectedColor = getResources().getColor(R.color.red_primary);

        switch (position) {
            case 0:
                tabAll.setTextColor(selectedColor);
                break;
            case 1:
                tabProducts.setTextColor(selectedColor);
                break;
            case 2:
                tabShops.setTextColor(selectedColor);
                break;
        }
    }

    private void resetTabColors() {
        int normalColor = getResources().getColor(R.color.gray_dark);
        tabAll.setTextColor(normalColor);
        tabProducts.setTextColor(normalColor);
        tabShops.setTextColor(normalColor);
    }

    private void loadDataByCategory(String category) {
        // 清空列表
        favoriteList.clear();

        // 更新UI
        updateUI();
    }

    private void initData() {
        favoriteList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new FavoriteAdapter(this, favoriteList, isEditMode);
        rvFavorites.setLayoutManager(new GridLayoutManager(this, 2));
        rvFavorites.setAdapter(adapter);
    }

    private void updateUI() {
        if (favoriteList.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            adapter.setEditMode(isEditMode);
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        tvEdit.setText(isEditMode ? "完成" : "编辑");

        // 更新选中项列表
        adapter.setEditMode(isEditMode);
        adapter.notifyDataSetChanged();

        if (isEditMode) {
            Toast.makeText(this, "进入编辑模式，可选择要取消收藏的内容", Toast.LENGTH_SHORT).show();
        } else {
            // 执行删除操作
            deleteSelectedItems();
            Toast.makeText(this, "已保存更改", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSelectedItems() {
        List<FavoriteItem> selectedItems = adapter.getSelectedItems();
        if (!selectedItems.isEmpty()) {
            favoriteList.removeAll(selectedItems);
            adapter.clearSelection();
            updateUI();
            Toast.makeText(this, "取消了 " + selectedItems.size() + " 个收藏", Toast.LENGTH_SHORT).show();
        }
    }
}