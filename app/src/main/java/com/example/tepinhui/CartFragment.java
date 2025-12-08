package com.example.tepinhui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CartFragment extends Fragment {

    // 静态工厂方法 (可选，但推荐用于传递参数)
    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // ！！！注意：这里的 R.layout.fragment_cart 假设您已创建对应的布局文件
        // ！！！如果您还没有创建布局，可以暂时使用一个简单的TextView或返回null。
        // 建议：右键点击 res/layout，新建一个名为 fragment_cart.xml 的布局文件
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
}
