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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

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
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
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

        NetworkUtils.get(
                "/api/products?page=1&size=20&keyword=" + keyword,
                type,
                new NetworkUtils.Callback<Result<ProductListDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductListDTO> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            productList.clear();
                            productList.addAll(result.getData().getList());
                            adapter.notifyDataSetChanged();
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
