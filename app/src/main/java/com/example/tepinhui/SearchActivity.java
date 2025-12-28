package com.example.tepinhui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.example.tepinhui.ui.product.ProductAdapter;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class SearchActivity extends AppCompatActivity {

    public static final String EXTRA_KEYWORD = "extra_keyword";

    private EditText etSearch;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductDTO> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initRecyclerView();
        initListener();

        // 支持从外部带入 keyword（例如“点击首页搜索框但已预填”）
        String preset = getIntent() != null ? getIntent().getStringExtra(EXTRA_KEYWORD) : null;
        if (preset != null) {
            preset = preset.trim();
            if (!preset.isEmpty()) {
                etSearch.setText(preset);
                etSearch.setSelection(preset.length());
                searchProducts(preset);
            }
        }
    }

    private void initView() {
        etSearch = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.rv_search_result);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        // 点击搜索
        findViewById(R.id.tv_search).setOnClickListener(v -> doSearch());

        // 键盘搜索
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                doSearch();
                return true;
            }
            return false;
        });
    }

    private void doSearch() {
        String keyword = etSearch.getText().toString().trim();

        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }

        searchProducts(keyword);
    }

    private void searchProducts(String keyword) {
        Type type = new TypeToken<Result<ProductListDTO>>() {}.getType();

        String encoded;
        try {
            // 兼容中文/空格等字符，避免拼 URL 导致 400/解析失败
            encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            encoded = keyword;
        }

        NetworkUtils.get(
                "/api/products?page=1&size=20&keyword=" + encoded,
                type,
                new NetworkUtils.Callback<Result<ProductListDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductListDTO> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            productList.clear();
                            productList.addAll(result.getData().getList());
                            adapter.notifyDataSetChanged();
                            if (productList.isEmpty()) {
                                Toast.makeText(SearchActivity.this, "暂无搜索结果", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SearchActivity.this, result != null ? result.getMsg() : "搜索失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(SearchActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
