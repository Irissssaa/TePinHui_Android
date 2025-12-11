package com.example.tepinhui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RefundActivity extends AppCompatActivity {

    private TextView tabAll, tabProcessing, tabCompleted;
    private RecyclerView rvRefundList;
    private LinearLayout layoutEmpty;
    private RefundAdapter adapter;
    private List<RefundItem> refundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        initViews();
        setupListeners();
        initData();
        setupRecyclerView();

        // 默认选中全部
        selectTab(0);
        loadDataByStatus("all");
    }

    private void initViews() {
        tabAll = findViewById(R.id.tab_all);
        tabProcessing = findViewById(R.id.tab_processing);
        tabCompleted = findViewById(R.id.tab_completed);
        rvRefundList = findViewById(R.id.rv_refund_list);
        layoutEmpty = findViewById(R.id.layout_empty);

        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        // 状态标签点击
        tabAll.setOnClickListener(v -> {
            selectTab(0);
            loadDataByStatus("all");
        });

        tabProcessing.setOnClickListener(v -> {
            selectTab(1);
            loadDataByStatus("processing");
        });

        tabCompleted.setOnClickListener(v -> {
            selectTab(2);
            loadDataByStatus("completed");
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
                tabProcessing.setTextColor(selectedColor);
                break;
            case 2:
                tabCompleted.setTextColor(selectedColor);
                break;
        }
    }

    private void resetTabColors() {
        int normalColor = getResources().getColor(R.color.gray_dark);
        tabAll.setTextColor(normalColor);
        tabProcessing.setTextColor(normalColor);
        tabCompleted.setTextColor(normalColor);
    }

    private void loadDataByStatus(String status) {
        // 清空列表
        refundList.clear();

        // 模拟不同状态的数据
        switch (status) {
            case "all":
                // 所有退款/售后
                refundList.add(new RefundItem("T20231215001", "退款申请", "云南普洱茶", 128.00, "处理中"));
                refundList.add(new RefundItem("T20231214001", "退货退款", "新疆大枣", 59.90, "已完成"));
                refundList.add(new RefundItem("T20231213001", "换货申请", "金华火腿", 288.00, "处理中"));
                refundList.add(new RefundItem("T20231212001", "仅退款", "四川腊肉", 45.00, "已完成"));
                break;
            case "processing":
                // 处理中的退款/售后
                refundList.add(new RefundItem("T20231215001", "退款申请", "云南普洱茶", 128.00, "处理中"));
                refundList.add(new RefundItem("T20231213001", "换货申请", "金华火腿", 288.00, "处理中"));
                break;
            case "completed":
                // 已完成的退款/售后
                refundList.add(new RefundItem("T20231214001", "退货退款", "新疆大枣", 59.90, "已完成"));
                refundList.add(new RefundItem("T20231212001", "仅退款", "四川腊肉", 45.00, "已完成"));
                refundList.add(new RefundItem("T20231211001", "退货退款", "杭州丝绸", 399.00, "已完成"));
                break;
        }

        // 更新UI
        updateUI();
    }

    private void initData() {
        refundList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new RefundAdapter(this, refundList);
        rvRefundList.setLayoutManager(new LinearLayoutManager(this));
        rvRefundList.setAdapter(adapter);
    }

    private void updateUI() {
        if (refundList.isEmpty()) {
            rvRefundList.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvRefundList.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}