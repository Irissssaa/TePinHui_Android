package com.example.tepinhui.ui.category;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.CategoryDTO;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.example.tepinhui.dto.RegionDTO;
import com.example.tepinhui.ui.product.ProductAdapter;
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

    // 后端返回的「地区/品类」（带 id）
    private final List<RegionDTO> regions = new ArrayList<>();
    private final List<CategoryDTO> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initView();
        initRecyclerView();
        initTabs();

        // 默认：按地区
        switchToRegion();
        // 先拉取地区/品类（带 id），再按选择请求商品
        loadRegions();
    }

    private void initView() {
        tabRegion = findViewById(R.id.tab_region);
        tabCategory = findViewById(R.id.tab_category);
        recyclerCategory = findViewById(R.id.recycler_category);
        recyclerProducts = findViewById(R.id.recycler_products);
    }

    private void initRecyclerView() {
        // 左侧分类
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.setOnItemSelectedListener((position, name) -> {
            if (isRegionMode) {
                Integer regionId = findRegionIdByName(name);
                loadProducts(regionId, null);
            } else {
                Integer categoryId = findCategoryIdByName(name);
                loadProducts(null, categoryId);
            }
        });
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
        if (regions.isEmpty()) {
            // 兜底：没拉到后端数据时仍显示静态列表
            categoryList.addAll(Arrays.asList("云南", "四川", "新疆", "内蒙古"));
        } else {
            for (RegionDTO r : regions) {
                if (r != null && r.name != null) categoryList.add(r.name);
            }
        }
        categoryAdapter.notifyDataSetChanged();
    }

    /** 切换为「按品类」 */
    private void switchToCategory() {
        isRegionMode = false;

        tabRegion.setTextColor(getResources().getColor(R.color.gray_light));
        tabCategory.setTextColor(getResources().getColor(R.color.red_primary));

        categoryList.clear();
        if (categories.isEmpty()) {
            // 兜底：没拉到后端数据时仍显示静态列表
            categoryList.addAll(Arrays.asList("零食", "饮品", "手工艺", "调料"));
        } else {
            for (CategoryDTO c : categories) {
                if (c != null && c.name != null) categoryList.add(c.name);
            }
        }
        categoryAdapter.notifyDataSetChanged();
    }

    private void loadProducts(Integer regionId, Integer categoryId) {
        Type type = new TypeToken<Result<ProductListDTO>>() {}.getType();

        String url = "/api/products?page=1&size=20";
        if (regionId != null) url += "&regionId=" + regionId;
        if (categoryId != null) url += "&categoryId=" + categoryId;

        NetworkUtils.get(
                url,
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

    private void loadRegions() {
        Type type = new TypeToken<Result<List<RegionDTO>>>() {}.getType();
        NetworkUtils.get("/api/regions", type, new NetworkUtils.Callback<Result<List<RegionDTO>>>() {
            @Override
            public void onSuccess(Result<List<RegionDTO>> result) {
                regions.clear();
                if (result != null && result.isSuccess() && result.getData() != null) {
                    regions.addAll(result.getData());
                }
                loadCategories();
            }

            @Override
            public void onError(String msg) {
                loadCategories();
            }
        });
    }

    private void loadCategories() {
        Type type = new TypeToken<Result<List<CategoryDTO>>>() {}.getType();
        NetworkUtils.get("/api/categories", type, new NetworkUtils.Callback<Result<List<CategoryDTO>>>() {
            @Override
            public void onSuccess(Result<List<CategoryDTO>> result) {
                categories.clear();
                if (result != null && result.isSuccess() && result.getData() != null) {
                    categories.addAll(result.getData());
                }
                // 刷新左侧列表（按当前 tab）
                if (isRegionMode) switchToRegion(); else switchToCategory();
                // 默认加载一次商品
                if (isRegionMode) {
                    Integer rid = regions.isEmpty() ? null : regions.get(0).id;
                    loadProducts(rid, null);
                } else {
                    Integer cid = categories.isEmpty() ? null : categories.get(0).id;
                    loadProducts(null, cid);
                }
            }

            @Override
            public void onError(String msg) {
                loadProducts(null, null);
            }
        });
    }

    private Integer findRegionIdByName(String name) {
        if (name == null) return null;
        for (RegionDTO r : regions) {
            if (r != null && name.equals(r.name)) return r.id;
        }
        return null;
    }

    private Integer findCategoryIdByName(String name) {
        if (name == null) return null;
        for (CategoryDTO c : categories) {
            if (c != null && name.equals(c.name)) return c.id;
        }
        return null;
    }
}
