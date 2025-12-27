package com.example.tepinhui.ui.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;

import java.util.List;

public class PostHeaderAdapter extends RecyclerView.Adapter<PostHeaderAdapter.VH> {

    private CommunityPost post;

    public PostHeaderAdapter(CommunityPost post) {
        this.post = post;
    }

    public void updatePost(CommunityPost post) {
        this.post = post;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_detail_header, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        if (post == null) return;

        h.tvUserName.setText(post.getUserName());
        h.tvTime.setText(post.getTime());
        h.tvContent.setText(post.getContent());
        h.headerLikeCount.setText("赞 " + post.getLikeCount());
        h.headerCommentCount.setText("评论 " + post.getCommentCount());
        // avatar：你现在是 resId，没有就用占位
        int avatarRes = post.getAvatarResId();
        if (avatarRes != 0) {
            h.ivAvatar.setImageResource(avatarRes);
        } else {
            h.ivAvatar.setImageResource(R.mipmap.ic_launcher); // 换成你的默认头像占位
        }

        // post image：0 表示无图
        List<Integer> images = post.getImageList();
        if (images != null && !images.isEmpty()) {
            h.ivPostImage.setVisibility(View.VISIBLE);
            h.ivPostImage.setImageResource(images.get(0)); // 先展示第一张
        } else {
            h.ivPostImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 1; // header 永远只有一条
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivPostImage;
        TextView tvUserName, tvTime, tvContent, headerLikeCount, headerCommentCount;

        VH(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            headerLikeCount = itemView.findViewById(R.id.headerLikeCount);
            headerCommentCount = itemView.findViewById(R.id.headerCommentCount);
        }
    }
}
