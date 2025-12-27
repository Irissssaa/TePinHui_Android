package com.example.tepinhui.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.OrderStatsDTO;
import com.example.tepinhui.dto.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 用户相关API服务
 */
public class UserApiService {

    private static final String SHARED_PREF_NAME = "tepin_hui_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USERNAME = "username";

    // 登录响应数据结构
    public static class LoginResponse {
        private String token;
        private UserDTO user;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
    }

    /**
     * 用户注册
     */
    public static void register(String phone, String password, String username,
                                NetworkUtils.Callback<Result<UserDTO>> callback) {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        if (!TextUtils.isEmpty(username)) {
            params.put("username", username);
        }

        Type responseType = new TypeToken<Result<UserDTO>>(){}.getType();
        NetworkUtils.post("/api/auth/register", params, responseType, callback);
    }

    /**
     * 用户登录
     */
    public static void login(String phone, String password,
                             NetworkUtils.Callback<Result<LoginResponse>> callback) {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        Type responseType = new TypeToken<Result<LoginResponse>>(){}.getType();
        NetworkUtils.post("/api/auth/login", params, responseType, callback);
    }

    /**
     * 获取我的页面聚合数据
     */
    public static void getProfile(Context context, NetworkUtils.Callback<Result<Map<String, Object>>> callback) {
        String token = getToken(context);
        Log.d("UserApiService", "调用getProfile，token: " + (token.isEmpty() ? "空" : "存在"));

        if (token.isEmpty()) {
            Log.e("UserApiService", "用户未登录，token为空");
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();
        NetworkUtils.get("/api/user/profile", token, responseType, callback);
    }

    /**
     * 解析聚合数据中的订单统计
     */
    public static OrderStatsDTO parseOrderStats(Map<String, Object> profileData) {
        if (profileData == null || !profileData.containsKey("orderStats")) {
            return new OrderStatsDTO(0L, 0L, 0L, 0L, 0L);
        }

        try {
            Gson gson = new Gson();
            String json = gson.toJson(profileData.get("orderStats"));
            return gson.fromJson(json, OrderStatsDTO.class);
        } catch (Exception e) {
            Log.e("UserApiService", "解析订单统计失败: " + e.getMessage());
            return new OrderStatsDTO(0L, 0L, 0L, 0L, 0L);
        }
    }

    /**
     * 解析聚合数据中的用户信息
     */
    public static UserDTO parseUserInfo(Map<String, Object> profileData) {
        if (profileData == null || !profileData.containsKey("user")) {
            Log.e("UserApiService", "profileData中没有user字段");
            return null;
        }

        try {
            Gson gson = new Gson();
            String json = gson.toJson(profileData.get("user"));
            return gson.fromJson(json, UserDTO.class);
        } catch (Exception e) {
            Log.e("UserApiService", "解析用户信息失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取未读消息数量
     */
    public static long getUnreadMessageCount(Map<String, Object> profileData) {
        if (profileData == null || !profileData.containsKey("unreadMessages")) {
            return 0;
        }

        try {
            return ((Number) profileData.get("unreadMessages")).longValue();
        } catch (Exception e) {
            Log.e("UserApiService", "解析未读消息数量失败: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 保存用户信息和token到SharedPreferences
     */
    public static void saveUserInfo(Context context, String token, UserDTO user) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        Log.d("UserApiService", "保存用户信息，token长度: " + (token != null ? token.length() : 0));

        editor.putString(KEY_TOKEN, token);
        if (user != null) {
            editor.putInt(KEY_USER_ID, user.getId() != null ? user.getId() : 0);
            editor.putString(KEY_USER_PHONE, user.getPhone() != null ? user.getPhone() : "");
            editor.putString(KEY_USERNAME, user.getUsername() != null ? user.getUsername() : "");
            Log.d("UserApiService", "保存用户ID: " + user.getId() + ", 用户名: " + user.getUsername());
        }
        editor.apply();

        // 调试：验证保存是否成功
        String savedToken = prefs.getString(KEY_TOKEN, "");
        Log.d("UserApiService", "验证保存结果，token是否为空: " + savedToken.isEmpty());
    }

    /**
     * 清除用户信息
     */
    public static void clearUserInfo(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // 同时清除旧的sp_user
        context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).edit().clear().apply();
    }

    /**
     * 用户退出登录
     */
    public static void logout(Context context, NetworkUtils.Callback<Result<Void>> callback) {
        String token = getToken(context);

        if (TextUtils.isEmpty(token)) {
            // 如果没有token，直接清除本地数据
            clearUserInfo(context);
            if (callback != null) {
                Result<Void> result = Result.success(null, "退出成功");
                callback.onSuccess(result);
            }
            return;
        }

        Map<String, String> params = new java.util.HashMap<>();

        Type responseType = new TypeToken<Result<Void>>(){}.getType();

        // 发送退出登录请求
        NetworkUtils.post("/api/auth/logout", params, token, responseType,
                new NetworkUtils.Callback<Result<Void>>() {
                    @Override
                    public void onSuccess(Result<Void> result) {
                        // 无论后端返回什么，都清除本地数据
                        clearUserInfo(context);

                        // 返回结果给调用者
                        if (callback != null) {
                            callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        // 即使网络请求失败，也清除本地数据
                        clearUserInfo(context);

                        if (callback != null) {
                            callback.onError(msg);
                        }
                    }
                });
    }

    /**
     * 获取用户信息（检查登录状态）
     */
    public static void checkLoginStatus(Context context, NetworkUtils.Callback<Boolean> callback) {
        String token = getToken(context);

        if (TextUtils.isEmpty(token)) {
            callback.onError("未登录");
            return;
        }

        // 可以调用一个验证token的接口
        // 这里简化处理，直接检查本地token
        if (isLoggedIn(context)) {
            callback.onSuccess(true);
        } else {
            callback.onError("登录已失效");
        }
    }

    /**
     * 获取保存的token
     */
    public static String getToken(Context context) {
        // 优先从新的SharedPreferences获取
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, "");

        // 如果从新位置获取不到，尝试从旧位置获取（兼容性）
        if (token.isEmpty()) {
            android.content.SharedPreferences oldPrefs = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
            token = oldPrefs.getString("token", "");
            if (!token.isEmpty()) {
                Log.d("UserApiService", "从旧位置获取到token，长度: " + token.length());
            }
        }

        Log.d("UserApiService", "获取token结果: " + (token.isEmpty() ? "空" : "长度=" + token.length()));
        return token;
    }

    /**
     * 获取用户ID
     */
    public static int getUserId(Context context) {
        // 优先从新的SharedPreferences获取
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userId = prefs.getInt(KEY_USER_ID, 0);

        // 如果从新位置获取不到，尝试从旧位置获取
        if (userId == 0) {
            userId = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).getInt("userId", 0);
        }

        return userId;
    }

    /**
     * 获取用户名
     */
    public static String getUsername(Context context) {
        // 优先从新的SharedPreferences获取
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, "");

        // 如果从新位置获取不到，尝试从旧位置获取
        if (username.isEmpty()) {
            username = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).getString("username", "");
        }

        return username;
    }

    /**
     * 检查是否已登录
     */
    public static boolean isLoggedIn(Context context) {
        String token = getToken(context);
        boolean loggedIn = !TextUtils.isEmpty(token);
        Log.d("UserApiService", "检查登录状态: " + (loggedIn ? "已登录" : "未登录"));
        return loggedIn;
    }
}