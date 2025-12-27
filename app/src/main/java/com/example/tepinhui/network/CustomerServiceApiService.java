package com.example.tepinhui.network;

import android.content.Context;

import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 客服API服务
 */
public class CustomerServiceApiService {

    /**
     * 获取客服联系方式
     */
    public static void getContactInfo(Context context,
                                      NetworkUtils.Callback<Result<Map<String, Object>>> callback) {
        // 这个接口不需要强制登录，但仍然需要传递token（可以为空或使用一个通用的token）
        String token = UserApiService.getToken(context);

        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();

        // 如果用户已登录，传递token；如果未登录，传递空字符串
        if (!token.isEmpty()) {
            NetworkUtils.get("/api/customer-service/contact", token, responseType, callback);
        } else {
            // 对于不需要登录的接口，可以使用一个公共的token或空字符串
            // 这取决于后端API的设计
            NetworkUtils.get("/api/customer-service/contact", "", responseType, callback);
        }
    }
}