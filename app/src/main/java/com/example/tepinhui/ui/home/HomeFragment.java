package com.example.tepinhui.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.SearchActivity;
import com.example.tepinhui.ui.category.CategoryActivity;
import com.example.tepinhui.ui.product.ProductAdapter;
import com.example.tepinhui.dto.BannerDTO;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.dto.ProductListDTO;
import com.example.tepinhui.ui.story.StoryListActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // ===== Banner =====
    private RecyclerView bannerRecyclerView;
    private BannerAdapter bannerAdapter;
    private List<BannerDTO> bannerList;

    // ===== Products =====
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<ProductDTO> productList;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ===== 首页入口 =====
        View layoutSearch = view.findViewById(R.id.layout_search);
        View btnCategory = view.findViewById(R.id.entry_category);
        View btnHot = view.findViewById(R.id.entry_hot);
        View btnStory = view.findViewById(R.id.entry_story);

        layoutSearch.setOnClickListener(v -> {
            // 先用占位 SearchActivity，没有也没关系
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        btnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            intent.putExtra("mode", "category");
            startActivity(intent);
        });

        btnHot.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            intent.putExtra("mode", "hot"); // 预留
            startActivity(intent);
        });

        btnStory.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), StoryListActivity.class));
        });

        // ---------- Banner 初始化 ----------
        bannerRecyclerView = view.findViewById(R.id.recycler_banner);
        bannerList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(getContext(), bannerList);

        LinearLayoutManager bannerLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecyclerView.setLayoutManager(bannerLayoutManager);
        bannerRecyclerView.setAdapter(bannerAdapter);

        // ---------- 商品列表初始化 ----------
        productRecyclerView = view.findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

        // ---------- 加载数据 ----------
        loadBanners();
        loadProducts();

        return view;
    }

    // ================= 商品列表 =================
    private void loadProducts() {
        Type type = new TypeToken<Result<ProductListDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/products?page=1&size=10",
                type,
                new NetworkUtils.Callback<Result<ProductListDTO>>() {
                    @Override
                    public void onSuccess(Result<ProductListDTO> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            productList.clear();
                            productList.addAll(result.getData().getList());
                            productAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getActivity() == null) return;

                        Toast.makeText(
                                getActivity(),
                                "商品加载失败",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    // ================= Banner =================
    private void loadBanners() {
        Type type = new TypeToken<Result<List<BannerDTO>>>() {}.getType();

        NetworkUtils.get(
                "/api/banners",
                type,
                new NetworkUtils.Callback<Result<List<BannerDTO>>>() {
                    @Override
                    public void onSuccess(Result<List<BannerDTO>> result) {
                        android.util.Log.d("HomeFragment", "banners result=" + new com.google.gson.Gson().toJson(result));

                        if (result != null && result.isSuccess() && result.getData() != null) {
                            android.util.Log.d("HomeFragment", "banners size=" + result.getData().size());
                            bannerList.clear();
                            bannerList.addAll(result.getData());
                            bannerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        // Banner 失败一般不强提示，避免影响体验
                        if (getActivity() == null) return;

                        Toast.makeText(
                                getActivity(),
                                "Banner 加载失败",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}
