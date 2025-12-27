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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.VH> {

    private final List<CommunityComment> commentList;

    public CommentAdapter(List<CommunityComment> commentList) {
        this.commentList = commentList;
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

        if (c.getParentId() != null && c.getReplyToUser() != null) {
            h.tvContent.setText("回复 @" + c.getReplyToUser() + "：" + c.getContent());
        } else {
            h.tvContent.setText(c.getContent());
        }

        if (c.getAvatarResId() != 0) {
            h.ivAvatar.setImageResource(c.getAvatarResId());
        } else {
            h.ivAvatar.setImageResource(R.mipmap.ic_launcher);
        }
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
        TextView tvUserName, tvContent, tvTime;

        VH(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
