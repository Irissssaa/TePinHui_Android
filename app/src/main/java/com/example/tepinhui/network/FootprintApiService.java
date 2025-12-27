package com.example.tepinhui.network;

import android.content.Context;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.ProductDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 足迹API服务
 */
public class FootprintApiService {

    /**
     * 获取足迹列表
     */
    public static void getFootprints(Context context,
                                     NetworkUtils.Callback<Result<List<ProductDTO>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<List<ProductDTO>>>(){}.getType();
        NetworkUtils.get("/api/footprints", token, responseType, callback);
    }

    /**
     * 添加足迹（浏览商品时调用）
     */
    public static void addFootprint(Context context, int productId,
                                    NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        Map<String, Integer> request = new java.util.HashMap<>();
        request.put("productId", productId);
        NetworkUtils.post("/api/footprints", request, token, responseType, callback);
    }

    /**
     * 删除足迹
     */
    public static void deleteFootprint(Context context, int footprintId,
                                       NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.get("/api/footprints/" + footprintId + "/delete", token, responseType, callback);
    }

    /**
     * 清空足迹
     */
    public static void clearFootprints(Context context,
                                       NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/footprints/clear", null, token, responseType, callback);
    }

    /**
     * 批量删除足迹
     */
    public static void batchDeleteFootprints(Context context, List<Integer> footprintIds,
                                             NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/footprints/batch-delete", footprintIds, token, responseType, callback);
    }
}