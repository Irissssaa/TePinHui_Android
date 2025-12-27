package com.example.tepinhui.ui.story;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.StoryDetailDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class StoryDetailActivity extends AppCompatActivity {

    private ImageView ivCover;
    private TextView tvTitle;
    private TextView tvContent;
    private RecyclerView recyclerProducts;

    private int storyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        storyId = getIntent().getIntExtra("storyId", -1);
        if (storyId <= 0) {
            finish();
            return;
        }

        ivCover = findViewById(R.id.iv_cover);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        recyclerProducts = findViewById(R.id.recycler_products);

        recyclerProducts.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        loadStoryDetail(storyId);
    }

    private void loadStoryDetail(int id) {
        Type type = new TypeToken<Result<StoryDetailDTO>>() {}.getType();

        NetworkUtils.get(
                "/api/stories/" + id,
                type,
                new NetworkUtils.Callback<Result<StoryDetailDTO>>() {
                    @Override
                    public void onSuccess(Result<StoryDetailDTO> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            bindStory(result.getData());
                        } else {
                            Toast.makeText(
                                    StoryDetailActivity.this,
                                    result != null ? result.getMsg() : "加载失败",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(
                                StoryDetailActivity.this,
                                "加载失败：" + msg,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void bindStory(StoryDetailDTO s) {

        tvTitle.setText(s.getTitle());
        tvContent.setText(s.getContent());

        Glide.with(this)
                .load(s.getCoverUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivCover);

        // 关联商品（可为空）
        if (s.getProducts() != null && !s.getProducts().isEmpty()) {
            StoryProductAdapter adapter =
                    new StoryProductAdapter(this, s.getProducts());
            recyclerProducts.setAdapter(adapter);
            recyclerProducts.setVisibility(View.VISIBLE);
        } else {
            recyclerProducts.setVisibility(View.GONE);
        }
    }


}

