package com.example.tepinhui.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.example.tepinhui.ui.product.ProductAdapter;
import com.example.tepinhui.ui.story.ProvinceMapActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    // 顶部切换
    private TextView tabRegion;
    private TextView tabCategory;

    // 左右 RecyclerView
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerProducts;

    // Adapter & Data
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<String> categoryList = new ArrayList<>();
    private List<ProductDTO> productList = new ArrayList<>();

    // 当前模式：region / category
    private boolean isRegionMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initView();
        initRecyclerView();
        initTabs();

        // 默认：按地区
        switchToRegion();
        loadProducts();
    }

    private void initView() {
        tabRegion = findViewById(R.id.tab_region);
        tabCategory = findViewById(R.id.tab_category);
        recyclerCategory = findViewById(R.id.recycler_category);
        recyclerProducts = findViewById(R.id.recycler_products);

        View mapEntry = findViewById(R.id.iv_map_entry);
        if (mapEntry != null) {
            mapEntry.setOnClickListener(v -> startActivity(new Intent(this, ProvinceMapActivity.class)));
        }
    }

    private void initRecyclerView() {
        // 左侧分类
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerCategory.setAdapter(categoryAdapter);

        // 右侧商品
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(productAdapter);
    }

    private void initTabs() {
        tabRegion.setOnClickListener(v -> switchToRegion());
        tabCategory.setOnClickListener(v -> switchToCategory());
    }

    /** 切换为「按地区」 */
    private void switchToRegion() {
        isRegionMode = true;

        tabRegion.setTextColor(getResources().getColor(R.color.red_primary));
        tabCategory.setTextColor(getResources().getColor(R.color.gray_medium));

        categoryList.clear();
        categoryList.addAll(Arrays.asList("云南", "四川", "新疆", "内蒙古"));
        categoryAdapter.notifyDataSetChanged();
    }

    /** 切换为「按品类」 */
    private void switchToCategory() {
        isRegionMode = false;

        tabRegion.setTextColor(getResources().getColor(R.color.gray_light));
        tabCategory.setTextColor(getResources().getColor(R.color.red_primary));

        categoryList.clear();
        categoryList.addAll(Arrays.asList("零食", "饮品", "手工艺", "调料"));
        categoryAdapter.notifyDataSetChanged();
    }

    /** 加载商品（当前阶段：不区分分类，直接加载） */
    private void loadProducts() {
        Type type = new TypeToken<Result<ProductListDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/products?page=1&size=20",
                type,
                new NetworkUtils.Callback<Result<ProductListDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductListDTO> result) {
                        if (result != null && result.isSuccess()) {
                            productList.clear();
                            productList.addAll(result.getData().getList());
                            productAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        // 可以先不处理
                    }
                }
        );
    }
}
