package com.example.tepinhui.ui.story;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.example.tepinhui.ui.product.ProductAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 省份商品列表：复用 ProductAdapter，根据 ProductDTO.origin 做前端过滤
 */
public class ProvinceProductsActivity extends AppCompatActivity {

    public static final String EXTRA_PROVINCE_NAME = "provinceName";

    private TextView tvTitle;
    private TextView tvEmpty;
    private RecyclerView recyclerView;

    private final List<ProductDTO> data = new ArrayList<>();
    private ProductAdapter adapter;

    private String provinceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_products);

        provinceName = getIntent().getStringExtra(EXTRA_PROVINCE_NAME);
        if (provinceName == null) provinceName = "";

        tvTitle = findViewById(R.id.tv_title);
        tvEmpty = findViewById(R.id.tv_empty);
        recyclerView = findViewById(R.id.recycler_products);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        tvTitle.setText((provinceName.isEmpty() ? "省份" : provinceName) + "特产");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, data);
        recyclerView.setAdapter(adapter);

        loadProducts();
    }

    private void loadProducts() {
        // 后端日志未说明支持 origin 过滤；这里前端拉取后按 origin 过滤
        // size 取大一点提升命中率（仍属于前端改动）
        Type type = new TypeToken<Result<ProductListDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/products?page=1&size=100",
                type,
                new NetworkUtils.Callback<Result<ProductListDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductListDTO> result) {
                        if (result == null || !result.isSuccess() || result.getData() == null) {
                            showEmpty(result != null ? result.getMsg() : "加载失败");
                            return;
                        }

                        List<ProductDTO> list = result.getData().getList();
                        if (list == null || list.isEmpty()) {
                            showEmpty("暂无商品");
                            return;
                        }

                        List<ProductDTO> filtered = new ArrayList<>();
                        String target = provinceName.trim();
                        for (ProductDTO p : list) {
                            if (p == null) continue;
                            String origin = p.getOrigin();
                            if (origin == null) continue;
                            // 兼容 “云南/云南省/云南 · …” 等格式
                            if (target.isEmpty() || origin.contains(target) || target.contains(origin)) {
                                filtered.add(p);
                            }
                        }

                        if (filtered.isEmpty()) {
                            showEmpty("暂无「" + provinceName + "」相关特产");
                            return;
                        }

                        tvEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        data.clear();
                        data.addAll(filtered);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        showEmpty("加载失败：" + (msg == null ? "" : msg));
                    }
                }
        );
    }

    private void showEmpty(String tip) {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText(tip == null || tip.isEmpty() ? "暂无商品" : tip);
        Toast.makeText(this, tvEmpty.getText(), Toast.LENGTH_SHORT).show();
    }
}


