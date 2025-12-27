package com.example.tepinhui.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.AddressDTO;
import com.example.tepinhui.network.AddressApiService;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class EditAddressActivity extends AppCompatActivity {

    private EditText etReceiver, etPhone, etProvince, etCity, etDistrict, etDetail, etPostCode;
    private CheckBox cbDefault;
    private Button btnSave;
    private int addressId = -1; // -1表示新增，否则表示编辑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("编辑收货地址");
        }

        initViews();
        loadAddressData();
    }

    private void initViews() {
        etReceiver = findViewById(R.id.et_receiver);
        etPhone = findViewById(R.id.et_phone);
        etProvince = findViewById(R.id.et_province);
        etCity = findViewById(R.id.et_city);
        etDistrict = findViewById(R.id.et_district);
        etDetail = findViewById(R.id.et_detail);
        etPostCode = findViewById(R.id.et_post_code);
        cbDefault = findViewById(R.id.cb_default);
        btnSave = findViewById(R.id.btn_save);

        // 获取传递的地址ID
        addressId = getIntent().getIntExtra("address_id", -1);

        // 保存按钮点击事件
        btnSave.setOnClickListener(v -> saveAddress());

        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void loadAddressData() {
        if (addressId != -1) {
            // 加载现有地址数据
            // 这里需要调用获取单个地址详情的接口
            // 暂时先留空
        }
    }

    private void saveAddress() {
        // 验证输入
        if (!validateInput()) {
            return;
        }

        // 构建地址数据
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("receiver", etReceiver.getText().toString().trim());
        addressData.put("phone", etPhone.getText().toString().trim());
        addressData.put("province", etProvince.getText().toString().trim());
        addressData.put("city", etCity.getText().toString().trim());
        addressData.put("district", etDistrict.getText().toString().trim());
        addressData.put("detail", etDetail.getText().toString().trim());
        addressData.put("postCode", etPostCode.getText().toString().trim());
        addressData.put("isDefault", cbDefault.isChecked());

        if (addressId == -1) {
            // 新增地址
            addAddress(addressData);
        } else {
            // 更新地址
            updateAddress(addressData);
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etReceiver.getText())) {
            etReceiver.setError("请输入收货人姓名");
            etReceiver.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return false;
        }

        if (!isValidPhone(etPhone.getText().toString().trim())) {
            etPhone.setError("请输入有效的手机号码");
            etPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etProvince.getText())) {
            etProvince.setError("请输入省份");
            etProvince.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etCity.getText())) {
            etCity.setError("请输入城市");
            etCity.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etDetail.getText())) {
            etDetail.setError("请输入详细地址");
            etDetail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidPhone(String phone) {
        // 简单的手机号验证
        return phone.matches("^1[3-9]\\d{9}$");
    }

    private void addAddress(Map<String, Object> addressData) {
        AddressApiService.addAddress(this, addressData, new NetworkUtils.Callback<Result<AddressDTO>>() {
            @Override
            public void onSuccess(Result<AddressDTO> result) {
                if (result != null && result.isSuccess()) {
                    Toast.makeText(EditAddressActivity.this, "添加地址成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditAddressActivity.this,
                            "添加失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(EditAddressActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAddress(Map<String, Object> addressData) {
        AddressApiService.updateAddress(this, addressId, addressData,
                new NetworkUtils.Callback<Result<AddressDTO>>() {
                    @Override
                    public void onSuccess(Result<AddressDTO> result) {
                        if (result != null && result.isSuccess()) {
                            Toast.makeText(EditAddressActivity.this, "更新地址成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditAddressActivity.this,
                                    "更新失败: " + (result != null ? result.getMsg() : "未知错误"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(EditAddressActivity.this, "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}