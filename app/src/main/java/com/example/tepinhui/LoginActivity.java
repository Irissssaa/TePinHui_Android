package com.example.tepinhui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private CheckBox cbAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. 查找控件
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        cbAgreement = findViewById(R.id.cb_agreement);

        // 2. 监听登录按钮点击
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 检查用户协议是否勾选
        if (!cbAgreement.isChecked()) {
            Toast.makeText(this, "请先阅读并同意《用户协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查手机号和密码（此处仅作简单验证）
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

        // 模拟登录成功
        Toast.makeText(this, "登录成功，欢迎回来！", Toast.LENGTH_SHORT).show();

        // 登录成功后，跳转到主界面 (MainActivity)
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // 清除任务栈，防止按返回键回到登录页
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}