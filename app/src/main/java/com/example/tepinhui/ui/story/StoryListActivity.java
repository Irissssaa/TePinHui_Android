package com.example.tepinhui.ui.story;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.StoryDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private final List<StoryDTO> storyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        // 1. 绑定 RecyclerView
        recyclerView = findViewById(R.id.recycler_story);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StoryAdapter(this, storyList);
        recyclerView.setAdapter(adapter);

        // 2. 拉取故事列表
        loadStories();
    }

    private void loadStories() {
        Type type = new TypeToken<Result<List<StoryDTO>>>() {}.getType();

        NetworkUtils.get(
                "/api/stories",
                type,
                new NetworkUtils.Callback<Result<List<StoryDTO>>>() {
                    @Override
                    public void onSuccess(Result<List<StoryDTO>> result) {
                        if (result != null && result.isSuccess() && result.getData() != null) {
                            storyList.clear();
                            storyList.addAll(result.getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(
                                    StoryListActivity.this,
                                    result != null ? result.getMsg() : "加载失败",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(
                                StoryListActivity.this,
                                "加载故事失败：" + msg,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}
