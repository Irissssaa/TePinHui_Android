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
 * 收藏API服务
 */
public class FavoriteApiService {

    /**
     * 获取收藏列表
     */
    public static void getFavorites(Context context,
                                    NetworkUtils.Callback<Result<List<ProductDTO>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<List<ProductDTO>>>(){}.getType();
        NetworkUtils.get("/api/favorites", token, responseType, callback);
    }

    /**
     * 添加收藏
     */
    public static void addFavorite(Context context, int productId,
                                   NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/favorites/" + productId, null, token, responseType, callback);
    }

    /**
     * 删除收藏（取消收藏）
     */
    public static void removeFavorite(Context context, int productId,
                                      NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.get("/api/favorites/" + productId + "/delete", token, responseType, callback);
    }

    /**
     * 批量删除收藏
     */
    public static void batchRemoveFavorites(Context context, List<Integer> productIds,
                                            NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/favorites/batch-delete", productIds, token, responseType, callback);
    }

    /**
     * 检查商品是否已收藏
     */
    public static void checkFavorite(Context context, int productId,
                                     NetworkUtils.Callback<Result<Map<String, Boolean>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Map<String, Boolean>>>(){}.getType();
        NetworkUtils.get("/api/favorites/check/" + productId, token, responseType, callback);
    }
}