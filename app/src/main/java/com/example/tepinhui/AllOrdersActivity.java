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

public class AllOrdersActivity extends AppCompatActivity {

    private TextView tabAll, tabPendingPay, tabPendingShip, tabPendingReceive;
    private TextView tvEdit;
    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;
    private OrderAdapter adapter;
    private List<OrderItem> orderList;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

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
        tabPendingPay = findViewById(R.id.tab_pending_pay);
        tabPendingShip = findViewById(R.id.tab_pending_ship);
        tabPendingReceive = findViewById(R.id.tab_pending_receive);
        tvEdit = findViewById(R.id.tv_edit);
        rvOrders = findViewById(R.id.rv_orders);
        layoutEmpty = findViewById(R.id.layout_empty);

    }

    private void setupListeners() {
        // 编辑按钮点击
        tvEdit.setOnClickListener(v -> toggleEditMode());

        // 状态标签点击
        tabAll.setOnClickListener(v -> {
            selectTab(0);
            loadDataByStatus("all");
        });

        tabPendingPay.setOnClickListener(v -> {
            selectTab(1);
            loadDataByStatus("pending_pay");
        });

        tabPendingShip.setOnClickListener(v -> {
            selectTab(2);
            loadDataByStatus("pending_ship");
        });

        tabPendingReceive.setOnClickListener(v -> {
            selectTab(3);
            loadDataByStatus("pending_receive");
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
                tabPendingPay.setTextColor(selectedColor);
                break;
            case 2:
                tabPendingShip.setTextColor(selectedColor);
                break;
            case 3:
                tabPendingReceive.setTextColor(selectedColor);
                break;
        }
    }

    private void resetTabColors() {
        int normalColor = getResources().getColor(R.color.gray_dark);
        tabAll.setTextColor(normalColor);
        tabPendingPay.setTextColor(normalColor);
        tabPendingShip.setTextColor(normalColor);
        tabPendingReceive.setTextColor(normalColor);
    }

    private void loadDataByStatus(String status) {
        // 清空列表
        orderList.clear();

        // 模拟不同状态的数据
        switch (status) {
            case "all":
                // 所有订单
                orderList.add(new OrderItem("20231215001", "待付款", "云南普洱茶", 128.00, "2023-12-15"));
                orderList.add(new OrderItem("20231214001", "待发货", "新疆大枣", 59.90, "2023-12-14"));
                orderList.add(new OrderItem("20231213001", "待收货", "金华火腿", 288.00, "2023-12-13"));
                orderList.add(new OrderItem("20231212001", "已完成", "四川腊肉", 45.00, "2023-12-12"));
                break;
            case "pending_pay":
                // 待付款订单
                orderList.add(new OrderItem("20231215001", "待付款", "云南普洱茶", 128.00, "2023-12-15"));
                orderList.add(new OrderItem("20231215002", "待付款", "杭州丝绸", 399.00, "2023-12-15"));
                break;
            case "pending_ship":
                // 待发货订单
                orderList.add(new OrderItem("20231214001", "待发货", "新疆大枣", 59.90, "2023-12-14"));
                break;
            case "pending_receive":
                // 待收货订单
                orderList.add(new OrderItem("20231213001", "待收货", "金华火腿", 288.00, "2023-12-13"));
                orderList.add(new OrderItem("20231213002", "待收货", "西湖龙井", 198.00, "2023-12-13"));
                break;
        }

        // 更新UI
        updateUI();
    }

    private void initData() {
        orderList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(this, orderList, isEditMode);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
    }

    private void updateUI() {
        if (orderList.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
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
            Toast.makeText(this, "进入编辑模式，可选择订单进行操作", Toast.LENGTH_SHORT).show();
        } else {
            // 执行操作
            processSelectedOrders();
            Toast.makeText(this, "已保存更改", Toast.LENGTH_SHORT).show();
        }
    }

    private void processSelectedOrders() {
        List<OrderItem> selectedOrders = adapter.getSelectedOrders();
        if (!selectedOrders.isEmpty()) {
            // 这里可以根据需要执行删除、批量付款等操作
            Toast.makeText(this, "已选中 " + selectedOrders.size() + " 个订单", Toast.LENGTH_SHORT).show();
            adapter.clearSelection();
        }
    }
}