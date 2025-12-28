package com.example.tepinhui.network;

import android.content.Context;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.AddressDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 收货地址API服务
 */
public class AddressApiService {

    /**
     * 获取收货地址列表
     */
    public static void getAddressList(Context context,
                                      NetworkUtils.Callback<Result<List<AddressDTO>>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<List<AddressDTO>>>(){}.getType();
        NetworkUtils.get("/api/addresses", token, responseType, callback);
    }
    public static void getAddressDetail(Context context, int addressId,
                                        NetworkUtils.Callback<Result<AddressDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<AddressDTO>>(){}.getType();
        NetworkUtils.get("/api/addresses/" + addressId, token, responseType, callback);
    }

    /**
     * 获取默认地址
     */
    public static void getDefaultAddress(Context context,
                                         NetworkUtils.Callback<Result<AddressDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<AddressDTO>>(){}.getType();
        NetworkUtils.get("/api/addresses/default", token, responseType, callback);
    }

    /**
     * 新增收货地址
     */
    public static void addAddress(Context context, Map<String, Object> addressData,
                                  NetworkUtils.Callback<Result<AddressDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<AddressDTO>>(){}.getType();
        NetworkUtils.post("/api/addresses", addressData, token, responseType, callback);
    }

    /**
     * 更新收货地址
     */
    public static void updateAddress(Context context, int addressId, Map<String, Object> addressData,
                                     NetworkUtils.Callback<Result<AddressDTO>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<AddressDTO>>(){}.getType();
        NetworkUtils.post("/api/addresses/" + addressId, addressData, token, responseType, callback);
    }

    /**
     * 删除收货地址
     */
    public static void deleteAddress(Context context, int addressId,
                                     NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.get("/api/addresses/" + addressId + "/delete", token, responseType, callback);
    }

    /**
     * 设置默认地址
     */
    public static void setDefaultAddress(Context context, int addressId,
                                         NetworkUtils.Callback<Result<Void>> callback) {
        String token = UserApiService.getToken(context);
        if (token.isEmpty()) {
            callback.onError("用户未登录");
            return;
        }

        Type responseType = new TypeToken<Result<Void>>(){}.getType();
        NetworkUtils.post("/api/addresses/" + addressId + "/default", null, token, responseType, callback);
    }
}