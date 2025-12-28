package com.example.tepinhui.ui.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.dto.CommunityPostDTO;
import com.example.tepinhui.dto.PageDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommunityPostAdapter adapter;
    private final List<CommunityPost> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community_list, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CommunityPostAdapter(getContext(), postList, PostSource.HELP);
        recyclerView.setAdapter(adapter);

        loadHelpPosts();

        return view;
    }

    private void loadHelpPosts() {
        String urlPart = CommunityApi.posts("HELP", 1, 10);

        Type type = new TypeToken<Result<PageDTO<CommunityPostDTO>>>() {}.getType();

        NetworkUtils.get(urlPart, type, new NetworkUtils.Callback<Result<PageDTO<CommunityPostDTO>>>() {
            @Override
            public void onSuccess(Result<PageDTO<CommunityPostDTO>> result) {
                // 1) 判空/判失败
                if (result == null) {
                    toast("返回为空");
                    return;
                }
                // 如果你的 Result 有 code/msg（你们一般是 code=0 成功）
                // 这里用最稳妥的方式：data 为空就直接提示
                PageDTO<CommunityPostDTO> page = result.getData();
                if (page == null || page.list == null) {
                    postList.clear();
                    adapter.notifyDataSetChanged();
                    toast("暂无数据");
                    return;
                }

                // 2) 转换 DTO -> Model
                postList.clear();

                for (CommunityPostDTO dto : page.list) {
                    if (dto == null) continue;

                    CommunityPost post = new CommunityPost(
                            dto.userName,
                            AvatarUtil.forUser(dto.userName),
                            dto.content,
                            dto.createdAt,           // 后端返回的时间字符串
                            dto.likeCount,
                            dto.commentCount,
                            new ArrayList<>()        // 先不处理图片
                    );
                    post.setPostId(dto.id);
                    post.setAvatarUrl(dto.avatarUrl);
                    post.setImageUrls(dto.images);
                    postList.add(post);
                }

                // 3) 刷新
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                Log.e("HelpFragment", "loadHelpPosts error: " + msg);
                toast("加载失败: " + msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHelpPosts();
    }

    private void toast(String s) {
        if (getContext() == null) return;
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
