package com.example.tepinhui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private CheckBox cbAgreement;
    // 加一个Loading状态，防止用户狂点按钮
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boolean isLogin = getSharedPreferences("sp_user", MODE_PRIVATE)
                .getBoolean("isLogin", false);

        if (isLogin) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
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
        String phone = etPhone.getText().toString().trim(); // trim()去掉首尾空格
        String password = etPassword.getText().toString().trim();

        // 1.【先校验】客户端校验（必须在发请求之前！）
        // 检查用户协议是否勾选
        if (!cbAgreement.isChecked()) {
            Toast.makeText(this, "请先阅读并同意《用户协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查手机号和密码
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

        // 3.【发请求】
        Type type = new TypeToken<Result<UserDTO>>(){}.getType();
        NetworkUtils.post("/api/auth/login", params, type, new NetworkUtils.Callback<Result<UserDTO>>() {
            @Override
            public void onSuccess(Result<UserDTO> result){
                // 恢复按钮状态
                btnLogin.setEnabled(true);
                btnLogin.setText("登录");

                if (result != null && result.isSuccess()) {
                    // ============== 只有在这里才能跳转！ ==============
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    // 4. 【保存登录状态】
                    UserDTO user = result.getData();
                    if (user != null) {
                        getSharedPreferences("sp_user", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLogin", true)
                                .putInt("userId", user.getId() == null ? -1 : user.getId())
                                .putString("phone", user.getPhone())
                                .putString("username", user.getUsername())
                                .apply();
                    }
                    // 跳转主页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // 清除登录页面的历史，按返回键直接退出APP而不是回到登录页
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // 登录失败（密码错或账号不存在）
                    String errorMsg = (result != null && result.getMsg() != null) ? result.getMsg() : "登录失败";
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                // 恢复按钮状态
                btnLogin.setEnabled(true);
                btnLogin.setText("登录");
                Toast.makeText(LoginActivity.this, "网络请求失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
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