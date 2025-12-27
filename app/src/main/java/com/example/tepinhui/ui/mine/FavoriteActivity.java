package com.example.tepinhui.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.network.FavoriteApiService;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private TextView tabAll, tabProducts, tabShops;
    private TextView tvEdit;
    private RecyclerView rvFavorites;
    private LinearLayout layoutEmpty;
    private FavoriteAdapter adapter;
    private List<ProductDTO> favoriteList;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initViews();
        setupListeners();
        setupRecyclerView();

        // 默认选中全部
        selectTab(0);
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
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

        favoriteList = new ArrayList<>();
    }

    private void setupListeners() {
        // 编辑按钮点击
        tvEdit.setOnClickListener(v -> toggleEditMode());

        // 分类标签点击
        tabAll.setOnClickListener(v -> {
            selectTab(0);
            loadFavorites();
        });

        tabProducts.setOnClickListener(v -> {
            selectTab(1);
            loadFavorites();
        });

        tabShops.setOnClickListener(v -> {
            selectTab(2);
            loadFavorites();
        });
    }

    private void selectTab(int position) {
        resetTabColors();
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

    private void loadFavorites() {
        if (!UserApiService.isLoggedIn(this)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FavoriteApiService.getFavorites(this, new NetworkUtils.Callback<Result<List<ProductDTO>>>() {
            @Override
            public void onSuccess(Result<List<ProductDTO>> result) {
                if (result != null && result.isSuccess()) {
                    favoriteList.clear();
                    if (result.getData() != null) {
                        favoriteList.addAll(result.getData());
                    }
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Toast.makeText(FavoriteActivity.this,
                            "加载失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(FavoriteActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new FavoriteAdapter(this, favoriteList, isEditMode);
        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isEditMode) return;
                ProductDTO product = favoriteList.get(position);
                // 跳转到商品详情页
                Toast.makeText(FavoriteActivity.this, "跳转到商品: " + product.getName(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRemoveClick(int position) {
                if (!isEditMode) return;
                ProductDTO product = favoriteList.get(position);
                removeFavorite(product.getId(), position);
            }
        });

        rvFavorites.setLayoutManager(new GridLayoutManager(this, 2));
        rvFavorites.setAdapter(adapter);
    }

    private void updateUI() {
        if (favoriteList.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
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
            Toast.makeText(this, "进入编辑模式，可选择要取消收藏的内容", Toast.LENGTH_SHORT).show();
        } else {
            // 执行批量删除操作
            List<Integer> selectedIds = adapter.getSelectedProductIds();
            if (!selectedIds.isEmpty()) {
                batchRemoveFavorites(selectedIds);
            } else {
                Toast.makeText(this, "已保存更改", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeFavorite(int productId, int position) {
        FavoriteApiService.removeFavorite(this, productId, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                if (result != null && result.isSuccess()) {
                    favoriteList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(FavoriteActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(FavoriteActivity.this,
                            "取消收藏失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(FavoriteActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void batchRemoveFavorites(List<Integer> productIds) {
        FavoriteApiService.batchRemoveFavorites(this, productIds,
                new NetworkUtils.Callback<Result<Void>>() {
                    @Override
                    public void onSuccess(Result<Void> result) {
                        if (result != null && result.isSuccess()) {
                            // 从列表中移除已选中的商品
                            List<ProductDTO> toRemove = new ArrayList<>();
                            for (ProductDTO product : favoriteList) {
                                if (productIds.contains(product.getId())) {
                                    toRemove.add(product);
                                }
                            }
                            favoriteList.removeAll(toRemove);
                            adapter.clearSelection();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(FavoriteActivity.this,
                                    "取消了 " + productIds.size() + " 个收藏",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            Toast.makeText(FavoriteActivity.this,
                                    "批量删除失败: " + (result != null ? result.getMsg() : "未知错误"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(FavoriteActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}