package com.example.tepinhui.network;

import android.content.Context;

import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.OrderDTO;
import com.example.tepinhui.dto.OrderStatsDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 订单API服务
 */
public class OrderApiService {

    /**
     * 获取订单列表
     * @param status 订单状态：UNPAID(待付款), PAID(待发货), SHIPPED(待收货), RECEIVED(已收货), CANCELED(已取消)
     * @param page 页码
     * @param size 每页大小
     */
    public static void getOrderList(Context context, String status, Integer page, Integer size,
                                    NetworkUtils.Callback<Result<Map<String, Object>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        // 构建查询参数
        StringBuilder urlBuilder = new StringBuilder("/api/orders");
        boolean hasParam = false;

        if (status != null && !status.isEmpty()) {
            urlBuilder.append("?status=").append(status);
            hasParam = true;
        }

        if (page != null && page > 0) {
            urlBuilder.append(hasParam ? "&" : "?").append("page=").append(page);
            hasParam = true;
        }

        if (size != null && size > 0) {
            urlBuilder.append(hasParam ? "&" : "?").append("size=").append(size);
        }

        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();
        NetworkUtils.get(urlBuilder.toString(), token, responseType, callback);
    }

    /**
     * 获取订单详情
     */
    public static void getOrderDetail(Context context, int orderId,
                                      NetworkUtils.Callback<Result<OrderDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<OrderDTO>>(){}.getType();
        NetworkUtils.get("/api/orders/" + orderId, token, responseType, callback);
    }

    /**
     * 获取待评价订单
     */
    public static void getUnreviewOrders(Context context, Integer page, Integer size,
                                         NetworkUtils.Callback<Result<Map<String, Object>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        StringBuilder urlBuilder = new StringBuilder("/api/orders/unreview");
        boolean hasParam = false;

        if (page != null && page > 0) {
            urlBuilder.append("?page=").append(page);
            hasParam = true;
        }

        if (size != null && size > 0) {
            urlBuilder.append(hasParam ? "&" : "?").append("size=").append(size);
        }

        Type responseType = new TypeToken<Result<Map<String, Object>>>(){}.getType();
        NetworkUtils.get(urlBuilder.toString(), token, responseType, callback);
    }

    /**
     * 获取订单状态统计
     */
    public static void getOrderStatusCount(Context context,
                                           NetworkUtils.Callback<Result<OrderStatsDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<OrderStatsDTO>>(){}.getType();
        NetworkUtils.get("/api/orders/status-count", token, responseType, callback);
    }

    /**
     * 取消订单
     */
    public static void cancelOrder(Context context, int orderId,
                                   NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/orders/" + orderId + "/cancel", null, token, responseType, callback);
    }

    /**
     * 确认收货
     */
    public static void confirmOrder(Context context, int orderId,
                                    NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/orders/" + orderId + "/confirm", null, token, responseType, callback);
    }
}