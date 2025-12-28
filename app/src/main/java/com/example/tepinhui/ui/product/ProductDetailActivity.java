package com.example.tepinhui.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.ui.story.StoryDetailActivity;
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
    private TextView tvShortIntro;
    private TextView tvCultureNote;
    private LinearLayout layoutStoryEntry;
    private Button btnBuy;

    private int productId;
    private Integer storyId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 1) 取 productId
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
        tvShortIntro = findViewById(R.id.tv_short_intro);
        tvCultureNote = findViewById(R.id.tv_culture_note);
        layoutStoryEntry = findViewById(R.id.layout_story_entry);
        btnBuy = findViewById(R.id.btn_buy);

        // 3) 加入购物车
        btnBuy.setOnClickListener(v -> addToCart());

        // 4) 特产故事入口（先占位）
        layoutStoryEntry.setOnClickListener(v -> openStory());

        // 5) 拉取详情
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
                            Toast.makeText(
                                    ProductDetailActivity.this,
                                    result != null ? result.getMsg() : "加载失败",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(
                                ProductDetailActivity.this,
                                "加载失败：" + msg,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void bindProduct(ProductDTO p) {

        tvName.setText(p.getName());
        tvPrice.setText(String.format(Locale.CHINA, "¥%.2f", p.getPrice()));

        // meta：产地 + 销量
        if (p.getOrigin() == null || p.getOrigin().trim().isEmpty()) {
            tvMeta.setText("已售 " + p.getSales());
        } else {
            tvMeta.setText(p.getOrigin() + " · 已售 " + p.getSales());
        }

        // 商品主图
        Glide.with(this)
                .load(p.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivProductImage);

        // ===== 简短介绍 =====
        if (p.getShortIntro() != null && !p.getShortIntro().isEmpty()) {
            tvShortIntro.setText(p.getShortIntro());
            tvShortIntro.setVisibility(View.VISIBLE);
        } else {
            tvShortIntro.setVisibility(View.GONE);
        }

        // ===== 产地 / 文化说明 =====
        if (p.getCultureNote() != null && !p.getCultureNote().isEmpty()) {
            tvCultureNote.setText(p.getCultureNote());
            tvCultureNote.setVisibility(View.VISIBLE);
        } else {
            tvCultureNote.setVisibility(View.GONE);
        }

        // ===== 特产故事入口 =====
        storyId = p.getStoryId();
        if (storyId != null) {
            layoutStoryEntry.setVisibility(View.VISIBLE);
        } else {
            layoutStoryEntry.setVisibility(View.GONE);
        }
    }

    private void openStory() {
        if (storyId == null || storyId <= 0) {
            Toast.makeText(this, "该商品暂无故事", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra("storyId", storyId);
        startActivity(intent);
    }

    private void addToCart() {
        String token = UserApiService.getToken(this);

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("productId", productId);
        body.put("quantity", 1);

        Type type = new TypeToken<Result<Object>>() {}.getType();

        NetworkUtils.post(
                "/api/cart/items",
                body,
                token,
                type,
                new NetworkUtils.Callback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> result) {
                        if (result != null && result.isSuccess()) {
                            Toast.makeText(
                                    ProductDetailActivity.this,
                                    "已加入购物车",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    ProductDetailActivity.this,
                                    result != null ? result.getMsg() : "加入失败",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(
                                ProductDetailActivity.this,
                                "加入购物车失败：" + msg,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}
