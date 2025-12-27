package com.example.tepinhui.network;

import android.content.Context;
import android.text.TextUtils;

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
        public UserDTO getUser() { return user; }
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
        if (token.isEmpty()) {
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
            return new OrderStatsDTO(0L, 0L, 0L, 0L, 0L);
        }
    }

    /**
     * 解析聚合数据中的用户信息
     */
    public static UserDTO parseUserInfo(Map<String, Object> profileData) {
        if (profileData == null || !profileData.containsKey("user")) {
            return null;
        }

        try {
            Gson gson = new Gson();
            String json = gson.toJson(profileData.get("user"));
            return gson.fromJson(json, UserDTO.class);
        } catch (Exception e) {
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
            return 0;
        }
    }

    /**
     * 保存用户信息和token到SharedPreferences
     */
    public static void saveUserInfo(Context context, String token, UserDTO user) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_TOKEN, token);
        if (user != null) {
            editor.putInt(KEY_USER_ID, user.getId() != null ? user.getId() : 0);
            editor.putString(KEY_USER_PHONE, user.getPhone());
            editor.putString(KEY_USERNAME, user.getUsername());
        }
        editor.apply();
    }

    /**
     * 清除用户信息
     */
    public static void clearUserInfo(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 获取保存的token
     */
    public static String getToken(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, "");
    }

    /**
     * 获取用户ID
     */
    public static int getUserId(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, 0);
    }

    /**
     * 获取用户名
     */
    public static String getUsername(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    /**
     * 检查是否已登录
     */
    public static boolean isLoggedIn(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, "");
        return !TextUtils.isEmpty(token);
    }
}