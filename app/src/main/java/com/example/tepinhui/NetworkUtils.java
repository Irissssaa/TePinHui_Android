package com.example.tepinhui;

import static android.content.ContentValues.TAG;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    // 1. ⚠️此处最关键！要把这个 IP 改成 B同学电脑的 IP 地址
    // 如果是用模拟器，通常是 http://10.0.2.2:8080
    // 如果是用真机，通常是 http://192.168.x.x:8080
    // 注意：后端 LoginController 如果加了 /api 前缀，这里不要加，在调用的时候加
    private static final String BASE_URL = "http://10.101.229.155:8080";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
            .readTimeout(10, TimeUnit.SECONDS)    // 读取超时时间
            .build();

    private static final Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final MediaType MEDIA_TYPE_JSON =
            MediaType.parse("application/json; charset=utf-8");

    // 定义回调接口
    public interface Callback<T> {
        void onSuccess(T result);
        void onError(String msg);
    }

    public static <T> void post(String urlPart,
                                Map<String, String> params,
                                Type responseType,
                                Callback<T> callback) {

        String url = BASE_URL + urlPart;
        String json = gson.toJson(params);

        RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body() != null ? response.body().string() : "";

                Log.d(TAG, "服务器返回: " + resp);

                try {
                    T obj = gson.fromJson(resp, responseType);
                    mainHandler.post(() -> callback.onSuccess(obj));
                } catch (Exception e) {
                    mainHandler.post(() -> callback.onError("解析失败: " + e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    /**
     * 发送 POST 请求 (用于登录、注册)
     * @param urlPart 接口后半段，例如 "/api/login"
     * @param params  参数对象（可以是 Map，也可以是 Entity）
     * @param clazz   想要转换成的类（例如 Result.class）
     * @param callback 回调
     */
    public static <T> void post(String urlPart, Object params, final Class<T> clazz, final Callback<T> callback) {
        // 1. 把参数转成 JSON 字符串
        String json = gson.toJson(params);

        // 2. 创建请求体
        RequestBody body = RequestBody.create(json, JSON);

        // 3. 创建请求对象
        Request request = new Request.Builder()
                .url(BASE_URL + urlPart)
                .post(body)
                .build();

        // 4. 发送请求
        sendRequest(request, clazz, callback);
    }

    /**
     * 发送需要登录态的 POST 请求（带 Authorization: Bearer token）
     */
    public static <T> void post(
            String urlPart,
            Object bodyObj,
            String token,
            Type responseType,
            Callback<T> callback
    ) {
        String url = BASE_URL + urlPart;

        // 1. JSON 序列化
        String json = gson.toJson(bodyObj);
        RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);

        // 2. 构建请求（重点：Authorization）
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        // 3. 复用 Type 版本的 sendRequest
        sendRequest(request, responseType, callback);
    }

    /**
     * 发送 GET 请求 (用于获取商品列表)
     * @param urlPart 接口后半段，例如 "/products"
     */
    public static <T> void get(String urlPart, final Class<T> clazz, final Callback<T> callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + urlPart)
                .get()
                .build();

        sendRequest(request, clazz, callback);
    }

    public static <T> void get(String urlPart, final java.lang.reflect.Type type, final Callback<T> callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + urlPart)
                .get()
                .build();

        sendRequest(request, type, callback);
    }

    public static <T> void get(
            String urlPart,
            String token,
            Type responseType,
            Callback<T> callback
    ) {
        Request request = new Request.Builder()
                .url(BASE_URL + urlPart)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        sendRequest(request, responseType, callback);
    }


    // 统一处理请求发送和结果解析
    private static <T> void sendRequest(Request request, final Class<T> clazz, final Callback<T> callback) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 网络请求失败（比如连不上网，或者IP错了）
                Log.e("NetworkUtils", "请求失败: " + e.getMessage());
                runOnUI(() -> callback.onError("网络无法连接，请检查IP或WiFi"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d("NetworkUtils", "服务器返回: " + responseBody);

                runOnUI(() -> {
                    try {
                        if (response.isSuccessful()) {
                            // 尝试把 JSON 解析成 Java 对象
                            T result = gson.fromJson(responseBody, clazz);
                            callback.onSuccess(result);
                        } else {
                            callback.onError("服务器错误: " + response.code());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("数据解析异常");
                    }
                });
            }
        });
    }
// 新增一个支持 Type 的 sendRequest
    private static <T> void sendRequest(
            Request request,
            final java.lang.reflect.Type type,
            final Callback<T> callback
    ) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUI(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = "";
                try {
                    json = response.body() != null ? response.body().string() : "";
                } catch (Exception ignored) {}

                if (!response.isSuccessful()) {
                    final int code = response.code();
                    final String body = json;
                    runOnUI(() -> {
                        Log.e("NetworkUtils", "服务器错误: " + code + ", body=" + body);
                        callback.onError("服务器错误: " + code);
                    });
                    return;
                }

                final String body = json;
                try {
                    final T result = gson.fromJson(body, type);
                    runOnUI(() -> {
                        if (result == null) {
                            callback.onError("返回为空");
                        } else {
                            callback.onSuccess(result);
                        }
                    });
                } catch (Exception e) {
                    runOnUI(() -> callback.onError("解析失败"));
                }
            }
        });
    }



    // 切换到主线程（更新UI必须在主线程）
    private static void runOnUI(Runnable r) {
        mainHandler.post(r);
    }
}