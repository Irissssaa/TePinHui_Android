package com.example.tepinhui.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.AddressDTO;
import com.example.tepinhui.network.AddressApiService;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    private RecyclerView rvAddressList;
    private LinearLayout layoutEmpty;
    private Button btnAddAddress;
    private List<AddressDTO> addressList;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("收货地址");
        }

        initViews();
        setupListeners();
        loadAddressList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新加载地址列表
        loadAddressList();
    }

    private void initViews() {
        rvAddressList = findViewById(R.id.rv_address_list);
        layoutEmpty = findViewById(R.id.layout_empty);
        btnAddAddress = findViewById(R.id.btn_add_address);

        addressList = new ArrayList<>();
        adapter = new AddressAdapter(this, addressList);

        rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        rvAddressList.setAdapter(adapter);
    }

    private void setupListeners() {
        // 添加新地址按钮
        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditAddressActivity.class);
            startActivity(intent);
        });

        // 设置适配器点击事件
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                AddressDTO address = addressList.get(position);
                Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                intent.putExtra("address_id", address.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                AddressDTO address = addressList.get(position);
                deleteAddress(address.getId(), position);
            }

            @Override
            public void onSetDefaultClick(int position) {
                AddressDTO address = addressList.get(position);
                setDefaultAddress(address.getId());
            }
        });
    }

    private void loadAddressList() {
        if (!UserApiService.isLoggedIn(this)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AddressApiService.getAddressList(this, new NetworkUtils.Callback<Result<List<AddressDTO>>>() {
            @Override
            public void onSuccess(Result<List<AddressDTO>> result) {
                if (result != null && result.isSuccess()) {
                    addressList.clear();
                    if (result.getData() != null) {
                        addressList.addAll(result.getData());
                    }
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Toast.makeText(AddressActivity.this,
                            "加载失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(AddressActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void deleteAddress(int addressId, int position) {
        AddressApiService.deleteAddress(this, addressId, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                if (result != null && result.isSuccess()) {
                    addressList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(AddressActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(AddressActivity.this,
                            "删除失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(AddressActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDefaultAddress(int addressId) {
        AddressApiService.setDefaultAddress(this, addressId, new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                if (result != null && result.isSuccess()) {
                    // 重新加载列表以更新默认状态
                    loadAddressList();
                    Toast.makeText(AddressActivity.this, "设置默认地址成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddressActivity.this,
                            "设置失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(AddressActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (addressList.isEmpty()) {
            rvAddressList.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvAddressList.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}