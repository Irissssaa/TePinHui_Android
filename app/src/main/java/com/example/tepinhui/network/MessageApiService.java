package com.example.tepinhui.network;

import android.content.Context;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.MessageDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 消息API服务
 */
public class MessageApiService {

    /**
     * 获取消息列表
     */
    public static void getMessages(Context context, Integer page, Integer size,
                                   NetworkUtils.Callback<Result<Map<String, Object>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        StringBuilder urlBuilder = new StringBuilder("/api/messages");
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
     * 获取消息详情
     */
    public static void getMessageDetail(Context context, int messageId,
                                        NetworkUtils.Callback<Result<MessageDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<MessageDTO>>(){}.getType();
        NetworkUtils.get("/api/messages/" + messageId, token, responseType, callback);
    }

    /**
     * 标记消息为已读
     */
    public static void markAsRead(Context context, int messageId,
                                  NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        Map<String, Boolean> request = new java.util.HashMap<>();
        request.put("read", true);
        NetworkUtils.post("/api/messages/" + messageId + "/read", request, token, responseType, callback);
    }

    /**
     * 获取未读消息数量
     */
    public static void getUnreadCount(Context context,
                                      NetworkUtils.Callback<Result<Map<String, Integer>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Map<String, Integer>>>(){}.getType();
        NetworkUtils.get("/api/messages/unread-count", token, responseType, callback);
    }

    /**
     * 批量标记为已读
     */
    public static void batchMarkAsRead(Context context, List<Integer> messageIds,
                                       NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/messages/batch-read", messageIds, token, responseType, callback);
    }

    /**
     * 删除消息
     */
    public static void deleteMessage(Context context, int messageId,
                                     NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.get("/api/messages/" + messageId + "/delete", token, responseType, callback);
    }
}