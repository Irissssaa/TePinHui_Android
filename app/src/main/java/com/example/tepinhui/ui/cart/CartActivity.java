package com.example.tepinhui.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.CartItemDTO;
import com.example.tepinhui.ui.auth.CartDTO;
import com.example.tepinhui.network.UserApiService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private LinearLayout layoutEmpty;
    private RecyclerView recyclerView;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private Button btnGoShopping;
    private CartAdapter adapter;
    private final List<CartItemDTO> cartList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Toast.makeText(this, "进入CartActivity", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        layoutEmpty = findViewById(R.id.layout_empty);
        recyclerView = findViewById(R.id.recycler_cart);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnGoShopping = findViewById(R.id.btn_go_shopping);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartList);
        recyclerView.setAdapter(adapter);

        btnCheckout.setOnClickListener(v ->
                Toast.makeText(this, "下单功能下一步实现", Toast.LENGTH_SHORT).show()
        );
        btnGoShopping.setOnClickListener(v -> {
            finish(); // 回到首页
        });

        loadCart();
    }

    private void loadCart() {
        String token = UserApiService.getToken(this);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        Type type = new TypeToken<Result<CartDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/cart",
                token,
                type,
                new NetworkUtils.Callback<Result<CartDTO>>() {
                    @Override
                    public void onSuccess(Result<CartDTO> result) {
                        List<CartItemDTO> items = result.getData().getItems();

                        if (items != null && !items.isEmpty()) {
                            layoutEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            cartList.clear();
                            cartList.addAll(items);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            layoutEmpty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(CartActivity.this,
                                "加载购物车失败：" + msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
