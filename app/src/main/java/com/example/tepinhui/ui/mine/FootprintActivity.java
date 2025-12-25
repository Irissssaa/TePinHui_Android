package com.example.tepinhui.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;

import java.util.ArrayList;
import java.util.List;

public class FootprintActivity extends AppCompatActivity {

    private TextView tabToday, tabWeek, tabMonth;
    private TextView tvEdit;
    private RecyclerView rvFootprint;
    private LinearLayout layoutEmpty;
    private FootprintAdapter adapter;
    private List<FootprintItem> footprintList;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);

        initViews();
        setupListeners();
        initData();
        setupRecyclerView();

        // 默认选中今天
        selectTab(0);
        loadDataByTime("today");
    }

    private void initViews() {
        tabToday = findViewById(R.id.tab_today);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tvEdit = findViewById(R.id.tv_edit);
        rvFootprint = findViewById(R.id.rv_footprint);
        layoutEmpty = findViewById(R.id.layout_empty);

        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        // 编辑按钮点击
        tvEdit.setOnClickListener(v -> toggleEditMode());

        // 时间标签点击
        tabToday.setOnClickListener(v -> {
            selectTab(0);
            loadDataByTime("today");
        });

        tabWeek.setOnClickListener(v -> {
            selectTab(1);
            loadDataByTime("week");
        });

        tabMonth.setOnClickListener(v -> {
            selectTab(2);
            loadDataByTime("month");
        });
    }

    private void selectTab(int position) {
        resetTabColors();

        int selectedColor = getResources().getColor(R.color.red_primary);

        switch (position) {
            case 0:
                tabToday.setTextColor(selectedColor);
                break;
            case 1:
                tabWeek.setTextColor(selectedColor);
                break;
            case 2:
                tabMonth.setTextColor(selectedColor);
                break;
        }
    }

    private void resetTabColors() {
        int normalColor = getResources().getColor(R.color.gray_dark);
        tabToday.setTextColor(normalColor);
        tabWeek.setTextColor(normalColor);
        tabMonth.setTextColor(normalColor);
    }

    private void loadDataByTime(String timeRange) {
        footprintList.clear();

        switch (timeRange) {
            case "today":
                footprintList.add(new FootprintItem("云南普洱茶", "今天 10:30", 128.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("新疆大枣", "今天 09:15", 59.90, R.drawable.ic_weixin_new));
                break;
            case "week":
                footprintList.add(new FootprintItem("金华火腿", "3天前", 288.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("四川腊肉", "5天前", 45.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("杭州丝绸", "6天前", 399.00, R.drawable.ic_weixin_new));
                break;
            case "month":
                footprintList.add(new FootprintItem("西湖龙井", "15天前", 198.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("阳澄湖大闸蟹", "20天前", 299.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("东北大米", "25天前", 68.00, R.drawable.ic_weixin_new));
                footprintList.add(new FootprintItem("宁夏枸杞", "28天前", 88.00, R.drawable.ic_weixin_new));
                break;
        }

        updateUI();
    }

    private void initData() {
        footprintList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new FootprintAdapter(footprintList, isEditMode);
        rvFootprint.setLayoutManager(new LinearLayoutManager(this));
        rvFootprint.setAdapter(adapter);
    }

    private void updateUI() {
        if (footprintList.isEmpty()) {
            rvFootprint.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFootprint.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            adapter.setEditMode(isEditMode);
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        tvEdit.setText(isEditMode ? "完成" : "编辑");

        adapter.setEditMode(isEditMode);
        adapter.notifyDataSetChanged();

        if (isEditMode) {
            Toast.makeText(this, "进入编辑模式，可选择要删除的足迹", Toast.LENGTH_SHORT).show();
        } else {
            deleteSelectedItems();
            Toast.makeText(this, "已保存更改", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSelectedItems() {
        List<FootprintItem> selectedItems = adapter.getSelectedItems();
        if (!selectedItems.isEmpty()) {
            footprintList.removeAll(selectedItems);
            adapter.clearSelection();
            updateUI();
            Toast.makeText(this, "删除了 " + selectedItems.size() + " 个足迹", Toast.LENGTH_SHORT).show();
        }
    }
}