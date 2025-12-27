package com.example.tepinhui.network;

import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 商品API服务
 */
public class ProductApiService {

    /**
     * 获取商品列表
     */
    public static void getProductList(Integer page, Integer size,
                                      NetworkUtils.Callback<Result<ProductListDTO>> callback) {
        StringBuilder urlBuilder = new StringBuilder("/api/products");
        boolean hasParam = false;

        if (page != null && page > 0) {
            urlBuilder.append("?page=").append(page);
            hasParam = true;
        }

        if (size != null && size > 0) {
            urlBuilder.append(hasParam ? "&" : "?").append("size=").append(size);
        }

        Type responseType = new TypeToken<Result<ProductListDTO>>(){}.getType();
        NetworkUtils.get(urlBuilder.toString(), responseType, callback);
    }

    /**
     * 获取商品详情
     */
    public static void getProductDetail(int productId,
                                        NetworkUtils.Callback<Result<ProductDTO>> callback) {
        Type responseType = new TypeToken<Result<ProductDTO>>(){}.getType();
        NetworkUtils.get("/api/products/" + productId, responseType, callback);
    }

    /**
     * 获取推荐商品
     */
    public static void getRecommendProducts(Integer limit,
                                            NetworkUtils.Callback<Result<java.util.List<ProductDTO>>> callback) {
        String url = "/api/products/recommend";
        if (limit != null && limit > 0) {
            url += "?limit=" + limit;
        }

        Type responseType = new TypeToken<Result<java.util.List<ProductDTO>>>(){}.getType();
        NetworkUtils.get(url, responseType, callback);
    }
}