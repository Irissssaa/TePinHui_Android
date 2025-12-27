package com.example.tepinhui.ui.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;

import java.util.List;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.VH> {

    private PostSource postSource;
    private Context context;
    private List<CommunityPost> postList;

    public CommunityPostAdapter(Context context,
                                List<CommunityPost> postList,
                                PostSource postSource) {
        this.context = context;
        this.postList = postList;
        this.postSource = postSource;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_community_post, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CommunityPost post = postList.get(position);
        h.tvUserName.setText(post.getUserName());
        h.tvContent.setText(post.getContent());
        h.tvTime.setText(post.getTime());
        h.tvLike.setText("👍 " + post.getLikeCount());
        h.tvComment.setText("💬 " + post.getCommentCount());

        h.itemView.setOnClickListener(v -> {
            Intent intent;

            if (postSource == PostSource.HELP) {
                intent = new Intent(context, HelpDetailActivity.class);
            } else {
                intent = new Intent(context, PostDetailActivity.class);
            }

            intent.putExtra("extra_post", post);
            context.startActivity(intent);
        });

        // ===== 头像 =====
        if (post.getAvatarResId() != 0) {
            h.ivAvatar.setImageResource(post.getAvatarResId());
        } else {
            h.ivAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
        }

        // ===== 帖子图片 =====
        if (post.getImageList() != null && !post.getImageList().isEmpty()) {
            h.ivPostImage.setVisibility(View.VISIBLE);
            h.ivPostImage.setImageResource(post.getImageList().get(0));
        } else {
            h.ivPostImage.setVisibility(View.GONE);
        }

        // ===== 假点赞交互 =====
        h.tvLike.setOnClickListener(v -> {
            if (!post.isLiked()) {
                post.setLiked(true);
                post.setLikeCount(post.getLikeCount() + 1);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "你已经点过赞了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        ImageView ivAvatar, ivPostImage;
        TextView tvUserName, tvTime, tvContent, tvLike, tvComment;

        VH(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }
}
