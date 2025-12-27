package com.example.tepinhui.ui.product;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.ProductDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvMeta;
    private TextView tvOrigin;
    private Button btnBuy;

    private int productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 1) 取 id
        productId = getIntent().getIntExtra("productId", -1);
        if (productId <= 0) {
            Toast.makeText(this, "商品参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2) 绑定视图
        ivProductImage = findViewById(R.id.iv_product_image);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);
        tvMeta = findViewById(R.id.tv_meta);
        tvOrigin = findViewById(R.id.tv_origin);
        tvOrigin = findViewById(R.id.tv_origin);
        btnBuy = findViewById(R.id.btn_buy);

        // 3) 按钮
        btnBuy.setOnClickListener(v -> {
            addToCart();
        });

        // 4) 拉取详情
        loadProductDetail(productId);
    }

    private void loadProductDetail(int id) {
        Type type = new TypeToken<Result<ProductDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/products/" + id,
                type,
                new NetworkUtils.Callback<Result<ProductDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductDTO> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            bindProduct(result.getData());
                        } else {
                            Toast.makeText(ProductDetailActivity.this,
                                    result != null ? result.getMsg() : "加载失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(ProductDetailActivity.this,
                                "加载失败：" + msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void bindProduct(ProductDTO p) {
        // 如果你的 ProductDTO 是 public field，请把 p.getXxx() 改成 p.xxx

        tvName.setText(p.getName());

        // 价格格式化：¥29.90
        tvPrice.setText(String.format(Locale.CHINA, "¥%.2f", p.getPrice()));

        String origin = p.getOrigin();
        if (origin == null || origin.trim().isEmpty()) {
            tvMeta.setText("已售 " + p.getSales());
        } else {
            tvMeta.setText(origin + " · 已售 " + p.getSales());
        }

        Glide.with(this)
                .load(p.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivProductImage);
    }

    private void addToCart() {
        // 1. 取 token
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        String token = sp.getString("token", null);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("productId", productId);
        body.put("quantity", 1);

        Type type = new TypeToken<Result<Object>>() {}.getType();

        // 3. 调用接口
        NetworkUtils.post(
                "/api/cart/items",
                body,
                token,   // Authorization: Bearer token
                type,
                new NetworkUtils.Callback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> result) {
                        if (result != null && result.isSuccess()) {
                            Toast.makeText(ProductDetailActivity.this,
                                    "已加入购物车",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailActivity.this,
                                    result != null ? result.getMsg() : "加入失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(ProductDetailActivity.this,
                                "加入购物车失败：" + msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
