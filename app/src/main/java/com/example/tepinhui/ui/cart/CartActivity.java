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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.Listener {

    private LinearLayout layoutEmpty;
    private RecyclerView recyclerView;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private Button btnGoShopping;

    private CartAdapter adapter;
    private final List<CartItemDTO> cartList = new ArrayList<>();

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        layoutEmpty = findViewById(R.id.layout_empty);
        recyclerView = findViewById(R.id.recycler_cart);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnGoShopping = findViewById(R.id.btn_go_shopping);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartList, this);
        recyclerView.setAdapter(adapter);

        btnCheckout.setOnClickListener(v ->
                Toast.makeText(this, "下单功能下一步实现", Toast.LENGTH_SHORT).show()
        );
        btnGoShopping.setOnClickListener(v -> finish());

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        token = sp.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCart();
    }

    private void loadCart() {
        Type type = new TypeToken<Result<CartDTO>>() {}.getType();

        NetworkUtils.get(
                CartApi.cart(),
                token,
                type,
                new NetworkUtils.Callback<Result<CartDTO>>() {
                    @Override
                    public void onSuccess(Result<CartDTO> result) {
                        CartDTO data = result.getData();
                        List<CartItemDTO> items = (data == null) ? null : data.getItems();

                        if (items != null && !items.isEmpty()) {
                            layoutEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            cartList.clear();
                            cartList.addAll(items);
                            adapter.notifyDataSetChanged();

                            calculateTotalPrice();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            layoutEmpty.setVisibility(View.VISIBLE);
                            calculateTotalPrice();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(CartActivity.this, "加载购物车失败：" + msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /** 合计金额 */
    private void calculateTotalPrice() {
        double total = 0;
        for (CartItemDTO item : cartList) {
            if (item.isChecked()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        tvTotalPrice.setText(String.format("合计：¥%.2f", total));
    }

    // ==========================
    // Adapter 回调：这里做接口同步
    // ==========================

    @Override
    public void onCheckedChanged(int productId, boolean newChecked, int position, boolean oldChecked) {
        calculateTotalPrice();

        Map<String, Object> body = new HashMap<>();
        body.put("checked", newChecked);
        body.put("quantity", cartList.get(position).getQuantity()); // 一并带上，避免后端只接受完整更新

        Type type = new TypeToken<Result<Object>>() {}.getType();

        NetworkUtils.put(
                CartApi.updateItem(productId),
                body,
                token,
                type,
                new NetworkUtils.Callback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> result) {
                        // 默认成功即可（可按 code 再判断）
                        calculateTotalPrice();
                    }

                    @Override
                    public void onError(String msg) {
                        // 回滚
                        if (position >= 0 && position < cartList.size()) {
                            cartList.get(position).setChecked(oldChecked);
                            adapter.notifyItemChanged(position);
                            calculateTotalPrice();
                        }
                        Toast.makeText(CartActivity.this, "更新勾选失败：" + msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onQuantityChanged(int productId, int newQty, int position, int oldQty) {
        calculateTotalPrice();

        Map<String, Object> body = new HashMap<>();
        body.put("quantity", newQty);
        body.put("checked", cartList.get(position).isChecked()); // 一并带上

        Type type = new TypeToken<Result<Object>>() {}.getType();

        NetworkUtils.put(
                CartApi.updateItem(productId),
                body,
                token,
                type,
                new NetworkUtils.Callback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> result) {
                        calculateTotalPrice();
                    }

                    @Override
                    public void onError(String msg) {
                        // 回滚
                        if (position >= 0 && position < cartList.size()) {
                            cartList.get(position).setQuantity(oldQty);
                            adapter.notifyItemChanged(position);
                            calculateTotalPrice();
                        }
                        Toast.makeText(CartActivity.this, "更新数量失败：" + msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onDeleteClicked(int productId, int position) {
        Type type = new TypeToken<Result<Object>>() {}.getType();

        NetworkUtils.delete(
                CartApi.deleteItem(productId),
                token,
                type,
                new NetworkUtils.Callback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> result) {
                        if (position >= 0 && position < cartList.size()) {
                            cartList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }

                        if (cartList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            layoutEmpty.setVisibility(View.VISIBLE);
                        }

                        calculateTotalPrice();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(CartActivity.this, "删除失败：" + msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
