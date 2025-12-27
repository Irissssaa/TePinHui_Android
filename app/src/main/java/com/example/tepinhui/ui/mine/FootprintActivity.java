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
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.network.FootprintApiService;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class FootprintActivity extends AppCompatActivity {

    private TextView tabToday, tabWeek, tabMonth;
    private TextView tvEdit;
    private RecyclerView rvFootprint;
    private LinearLayout layoutEmpty;
    private FootprintAdapter adapter;
    private List<ProductDTO> footprintList;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);

        initViews();
        setupListeners();
        setupRecyclerView();

        // 默认选中今天
        selectTab(0);
        loadFootprints();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFootprints();
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

        footprintList = new ArrayList<>();
    }

    private void setupListeners() {
        // 编辑按钮点击
        tvEdit.setOnClickListener(v -> toggleEditMode());

        // 时间标签点击
        tabToday.setOnClickListener(v -> {
            selectTab(0);
            loadFootprints();
        });

        tabWeek.setOnClickListener(v -> {
            selectTab(1);
            loadFootprints();
        });

        tabMonth.setOnClickListener(v -> {
            selectTab(2);
            loadFootprints();
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

    private void loadFootprints() {
        if (!UserApiService.isLoggedIn(this)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FootprintApiService.getFootprints(this, new NetworkUtils.Callback<Result<List<ProductDTO>>>() {
            @Override
            public void onSuccess(Result<List<ProductDTO>> result) {
                if (result != null && result.isSuccess()) {
                    footprintList.clear();
                    if (result.getData() != null) {
                        footprintList.addAll(result.getData());
                    }
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Toast.makeText(FootprintActivity.this,
                            "加载失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(FootprintActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new FootprintAdapter(footprintList, isEditMode);
        adapter.setOnItemClickListener(new FootprintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isEditMode) return;
                ProductDTO product = footprintList.get(position);
                // 跳转到商品详情页
                Toast.makeText(FootprintActivity.this, "跳转到商品: " + product.getName(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRemoveClick(int position) {
                if (!isEditMode) return;
                ProductDTO product = footprintList.get(position);
                deleteFootprint(position);
            }
        });

        rvFootprint.setLayoutManager(new LinearLayoutManager(this));
        rvFootprint.setAdapter(adapter);
    }

    private void updateUI() {
        if (footprintList.isEmpty()) {
            rvFootprint.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);
        } else {
            rvFootprint.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            tvEdit.setVisibility(View.VISIBLE);
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
            List<Integer> selectedPositions = adapter.getSelectedPositions();
            if (!selectedPositions.isEmpty()) {
                batchDeleteFootprints(selectedPositions);
            } else {
                Toast.makeText(this, "已保存更改", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteFootprint(int position) {
        // 由于后端接口需要足迹ID，这里我们简化处理
        // 实际开发中应该从ProductDTO中获取足迹ID
        Toast.makeText(this, "删除单个足迹", Toast.LENGTH_SHORT).show();
        footprintList.remove(position);
        adapter.notifyItemRemoved(position);
        updateUI();
    }

    private void batchDeleteFootprints(List<Integer> positions) {
        // 收集要删除的足迹ID
        List<Integer> footprintIds = new ArrayList<>();
        for (int position : positions) {
            // 这里需要足迹ID，简化处理
            // 实际开发中应该从ProductDTO中获取足迹ID
            footprintIds.add(position);
        }

        FootprintApiService.batchDeleteFootprints(this, footprintIds,
                new NetworkUtils.Callback<Result<Void>>() {
                    @Override
                    public void onSuccess(Result<Void> result) {
                        if (result != null && result.isSuccess()) {
                            // 从列表中移除已选中的足迹
                            List<ProductDTO> toRemove = new ArrayList<>();
                            for (int position : positions) {
                                if (position < footprintList.size()) {
                                    toRemove.add(footprintList.get(position));
                                }
                            }
                            footprintList.removeAll(toRemove);
                            adapter.clearSelection();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(FootprintActivity.this,
                                    "删除了 " + positions.size() + " 个足迹",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            Toast.makeText(FootprintActivity.this,
                                    "批量删除失败: " + (result != null ? result.getMsg() : "未知错误"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(FootprintActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}