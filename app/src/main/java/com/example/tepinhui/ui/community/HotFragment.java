package com.example.tepinhui.ui.community;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommunityPostAdapter adapter;
    private List<CommunityPost> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community_list, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CommunityPostAdapter(
                getContext(),
                postList,
                PostSource.HOT        // ⭐ 关键
        );
        recyclerView.setAdapter(adapter);

        loadHotPosts();

        return view;
    }

    private void loadHotPosts() {
        String url = CommunityApi.posts("HOT", 1, 10);

        java.lang.reflect.Type type =
                new com.google.gson.reflect.TypeToken<
                        Result<PageDTO<CommunityPostDTO>>
                        >() {}.getType();

        NetworkUtils.get(url, type, new NetworkUtils.Callback<
                Result<PageDTO<CommunityPostDTO>>
                >() {
            @Override
            public void onSuccess(Result<PageDTO<CommunityPostDTO>> result) {

                postList.clear();

                PageDTO<CommunityPostDTO> page = result.getData();
                if (page == null || page.list == null) return;

                for (CommunityPostDTO dto : page.list) {
                    CommunityPost post = new CommunityPost(
                            dto.userName,
                            R.drawable.avatar_1,
                            dto.content,
                            dto.createdAt,
                            dto.likeCount,
                            dto.commentCount,
                            new ArrayList<>()
                    );
                    post.setPostId(dto.id);
                    postList.add(post);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                // 可 Toast
            }
        });
    }
}
