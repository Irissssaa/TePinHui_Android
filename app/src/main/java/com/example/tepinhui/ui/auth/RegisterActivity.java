package com.example.tepinhui.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.UserDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private EditText etUsername;
    private CheckBox cbAgreement;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etUsername = findViewById(R.id.et_username);
        cbAgreement = findViewById(R.id.cb_agreement);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> handleRegister());

        TextView tvGoLogin = findViewById(R.id.tv_go_login);
        tvGoLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        if (!cbAgreement.isChecked()) {
            Toast.makeText(this, "请先阅读并同意《用户协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty() || phone.length() < 11) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "密码长度不能少于6位", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        // username 可选：为空就不传，让后端给默认值
        if (!username.isEmpty()) {
            params.put("username", username);
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("注册中...");

        Type responseType = new TypeToken<Result<UserDTO>>() {}.getType();

        NetworkUtils.post("/api/auth/register", params, responseType,
                new NetworkUtils.Callback<Result<UserDTO>>() {

                    @Override
                    public void onSuccess(Result<UserDTO> result) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("注册");

                        if (result != null && result.isSuccess()) {
                            Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();

                            // 回到登录页并回填手机号
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("phone", phone);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMsg = (result != null && result.getMsg() != null) ? result.getMsg() : "注册失败";
                            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("注册");
                        Toast.makeText(RegisterActivity.this, "网络请求失败: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
