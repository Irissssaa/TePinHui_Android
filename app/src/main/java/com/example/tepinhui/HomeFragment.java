package com.example.tepinhui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        initData();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initData() {
        productList = new ArrayList<>();
        productList.add(new Product("云南普洱茶", "陈年老茶，口感醇厚", 128.00, "[https://via.placeholder.com/300x300?text=Tea](https://via.placeholder.com/300x300?text=Tea)"));
        productList.add(new Product("新疆大枣", "个大皮薄，肉厚核小", 59.90, "[https://via.placeholder.com/300x300?text=Dates](https://via.placeholder.com/300x300?text=Dates)"));
        productList.add(new Product("金华火腿", "传统工艺，香气浓郁", 288.00, "[https://via.placeholder.com/300x300?text=Ham](https://via.placeholder.com/300x300?text=Ham)"));
        productList.add(new Product("四川腊肉", "麻辣鲜香，肥而不腻", 45.00, "[https://via.placeholder.com/300x300?text=Bacon](https://via.placeholder.com/300x300?text=Bacon)"));
        productList.add(new Product("杭州丝绸", "质地柔软，色彩艳丽", 399.00, "[https://via.placeholder.com/300x300?text=Silk](https://via.placeholder.com/300x300?text=Silk)"));
    }
}