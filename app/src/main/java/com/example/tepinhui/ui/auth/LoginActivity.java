package com.example.tepinhui.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.MainActivity;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.UserDTO;
import com.example.tepinhui.network.UserApiService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private CheckBox cbAgreement;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LoginActivity", "onCreate: 启动登录页面");

        // 检查是否已登录（使用UserApiService统一检查）
        if (UserApiService.isLoggedIn(this)) {
            Log.d("LoginActivity", "检测到已登录，跳转到主页");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        } else {
            Log.d("LoginActivity", "用户未登录，显示登录页面");
        }

        // 1. 查找控件
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        cbAgreement = findViewById(R.id.cb_agreement);
        TextView tvRegister = findViewById(R.id.tv_register);

        String phoneFromRegister = getIntent().getStringExtra("phone");
        if (phoneFromRegister != null && !phoneFromRegister.trim().isEmpty()) {
            etPhone.setText(phoneFromRegister);
            etPassword.requestFocus();
        }

        // 2. 监听登录按钮点击
        btnLogin.setOnClickListener(v -> handleLogin());

        // 3. 监听注册按钮点击
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1.【先校验】客户端校验
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

        // 2.【准备参数】
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        // 禁用按钮，防止重复点击
        btnLogin.setEnabled(false);
        btnLogin.setText("登录中...");

        // 3.【发请求】使用正确的返回类型
        Type type = new TypeToken<Result<UserApiService.LoginResponse>>(){}.getType();

        Log.d("LoginActivity", "发送登录请求，手机号: " + phone.substring(0, 3) + "****" + phone.substring(7));

        NetworkUtils.post("/api/auth/login", params, type,
                new NetworkUtils.Callback<Result<UserApiService.LoginResponse>>() {
                    @Override
                    public void onSuccess(Result<UserApiService.LoginResponse> result) {
                        // 恢复按钮状态
                        btnLogin.setEnabled(true);
                        btnLogin.setText("登录");

                        if (result != null && result.isSuccess()) {
                            UserApiService.LoginResponse loginResponse = result.getData();
                            if (loginResponse != null && loginResponse.getToken() != null) {
                                // 保存token和用户信息到统一的UserApiService
                                Log.d("LoginActivity", "登录成功，token长度: " + loginResponse.getToken().length());

                                UserApiService.saveUserInfo(LoginActivity.this,
                                        loginResponse.getToken(),
                                        loginResponse.getUser());

                                // 也保存到旧的sp_user以保持兼容性
                                saveToOldSharedPreferences(loginResponse);

                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                // 跳转主页
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("LoginActivity", "登录响应数据异常: token或user为null");
                                Toast.makeText(LoginActivity.this, "登录响应数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String errorMsg = (result != null && result.getMsg() != null)
                                    ? result.getMsg() : "登录失败";
                            Log.e("LoginActivity", "登录失败: " + errorMsg);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        // 恢复按钮状态
                        btnLogin.setEnabled(true);
                        btnLogin.setText("登录");
                        Log.e("LoginActivity", "网络请求失败: " + msg);
                        Toast.makeText(LoginActivity.this, "网络请求失败: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 为了兼容性，同时保存到旧的SharedPreferences
    private void saveToOldSharedPreferences(UserApiService.LoginResponse loginResponse) {
        if (loginResponse != null && loginResponse.getUser() != null) {
            UserDTO user = loginResponse.getUser();
            getSharedPreferences("sp_user", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLogin", true)
                    .putInt("userId", user.getId() != null ? user.getId() : -1)
                    .putString("phone", user.getPhone())
                    .putString("username", user.getUsername())
                    .putString("token", loginResponse.getToken())  // 这里也保存token
                    .apply();
            Log.d("LoginActivity", "已保存到旧的sp_user，userId: " + user.getId());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        String phoneFromRegister = intent.getStringExtra("phone");
        if (phoneFromRegister != null && !phoneFromRegister.trim().isEmpty()) {
            etPhone.setText(phoneFromRegister);
            etPassword.requestFocus();
        }
    }
}