package com.example.tepinhui.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.R;
import com.example.tepinhui.ui.service.OnlineServiceActivity;

public class ServiceCenterActivity extends AppCompatActivity {

    private LinearLayout layoutOrderQa, layoutPaymentQa, layoutDeliveryQa, layoutAfterSaleQa;
    private LinearLayout layoutContactService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("服务中心");
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        // 常见问题分类
        layoutOrderQa = findViewById(R.id.layout_order_qa);
        layoutPaymentQa = findViewById(R.id.layout_payment_qa);
        layoutDeliveryQa = findViewById(R.id.layout_delivery_qa);
        layoutAfterSaleQa = findViewById(R.id.layout_after_sale_qa);

        // 联系客服按钮
        layoutContactService = findViewById(R.id.layout_contact_service);
    }

    private void setupListeners() {
        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // 常见问题分类点击
        layoutOrderQa.setOnClickListener(v -> {
            Toast.makeText(this, "查看订单问题", Toast.LENGTH_SHORT).show();
        });

        layoutPaymentQa.setOnClickListener(v -> {
            Toast.makeText(this, "查看支付问题", Toast.LENGTH_SHORT).show();
        });

        layoutDeliveryQa.setOnClickListener(v -> {
            Toast.makeText(this, "查看配送问题", Toast.LENGTH_SHORT).show();
        });

        layoutAfterSaleQa.setOnClickListener(v -> {
            Toast.makeText(this, "查看售后问题", Toast.LENGTH_SHORT).show();
        });

        // 联系客服点击
        layoutContactService.setOnClickListener(v -> {
            // 跳转到在线客服页面
            Intent intent = new Intent(ServiceCenterActivity.this, OnlineServiceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}