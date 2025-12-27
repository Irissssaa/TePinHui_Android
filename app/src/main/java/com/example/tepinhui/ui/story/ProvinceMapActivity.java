package com.example.tepinhui.ui.story;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.ConsoleMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.R;

public class ProvinceMapActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_map);

        setTitle("中国地图");

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("ProvinceMapActivity", "console: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        // JS -> Android 回调
        webView.addJavascriptInterface(new JsBridge(), "Android");

        // 加载 assets 下的 HTML
        webView.loadUrl("file:///android_asset/china_province_map.html");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    private class JsBridge {
        @JavascriptInterface
        public void onProvinceClick(String provinceName) {
            runOnUiThread(() -> {
                Toast.makeText(ProvinceMapActivity.this, "点击省份：" + provinceName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProvinceMapActivity.this, ProvinceProductsActivity.class);
                intent.putExtra(ProvinceProductsActivity.EXTRA_PROVINCE_NAME, provinceName);
                startActivity(intent);
            });
        }
    }
}


