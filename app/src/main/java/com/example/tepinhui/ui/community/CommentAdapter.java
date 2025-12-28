package com.example.tepinhui.ui.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tepinhui.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.VH> {

    private final List<CommunityComment> commentList;
    private OnCommentClickListener onCommentClickListener;
    private OnCommentLongClickListener onCommentLongClickListener;

    public CommentAdapter(List<CommunityComment> commentList) {
        this.commentList = commentList;
    }

    public interface OnCommentClickListener {
        void onCommentClick(CommunityComment comment);
    }

    public interface OnCommentLongClickListener {
        void onCommentLongClick(CommunityComment comment, int position);
    }

    public void setOnCommentClickListener(OnCommentClickListener listener) {
        this.onCommentClickListener = listener;
    }

    public void setOnCommentLongClickListener(OnCommentLongClickListener listener) {
        this.onCommentLongClickListener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_comment, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CommunityComment c = commentList.get(position);

        h.tvUserName.setText(c.getUserName());
        h.tvTime.setText(c.getTime());
        h.tvLikeCount.setText("赞 " + c.getLikeCount());

        if (c.getParentId() != null && c.getReplyToUser() != null) {
            h.tvContent.setText("回复 @" + c.getReplyToUser() + "：" + c.getContent());
        } else {
            h.tvContent.setText(c.getContent());
        }

        if (c.getAvatarUrl() != null && !c.getAvatarUrl().trim().isEmpty()) {
            Glide.with(h.ivAvatar.getContext())
                    .load(c.getAvatarUrl())
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .error(R.drawable.ic_avatar_placeholder)
                    .into(h.ivAvatar);
        } else if (c.getAvatarResId() != 0) {
            h.ivAvatar.setImageResource(c.getAvatarResId());
        } else {
            h.ivAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
        }

        h.itemView.setOnClickListener(v -> {
            if (onCommentClickListener != null) {
                onCommentClickListener.onCommentClick(c);
            }
        });

        h.itemView.setOnLongClickListener(v -> {
            if (onCommentLongClickListener != null) {
                onCommentLongClickListener.onCommentLongClick(c, position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void replaceAll(List<CommunityComment> newList) {
        commentList.clear();
        commentList.addAll(newList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvUserName, tvContent, tvTime, tvLikeCount;

        VH(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
        }
    }
}
