package com.example.tepinhui.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tepinhui.R;
import com.example.tepinhui.dto.MessageDTO;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<MessageDTO> messageList;
    private OnItemClickListener listener;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(MessageDTO message);
        void onLongClick(MessageDTO message, int position);
    }

    public MessageAdapter(Context context, List<MessageDTO> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<MessageDTO> newList) {
        this.messageList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageDTO message = messageList.get(position);

        // 设置消息内容
        holder.tvTitle.setText(message.getTitle());
        holder.tvContent.setText(message.getContent());

        // 设置时间
        if (message.getCreatedAt() != null) {
            holder.tvTime.setText(dateFormat.format(message.getCreatedAt()));
        }

        // 设置未读标记
        if (message.getIsRead() != null && !message.getIsRead()) {
            holder.tvUnreadMark.setVisibility(View.VISIBLE);
        } else {
            holder.tvUnreadMark.setVisibility(View.GONE);
        }

        // 设置图标根据类型
        setMessageIcon(holder.ivIcon, message.getType());

        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(message);
            }
        });

        // 长按事件
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLongClick(message, position);
                return true;
            }
            return false;
        });
    }

    private void setMessageIcon(ImageView imageView, String type) {
        if (type == null) {
            // 使用默认图标
            imageView.setImageResource(R.drawable.news);
            return;
        }

        // 根据消息类型设置不同的图标
        // 使用项目中已有的图标，如果没有就使用默认图标
        switch (type.toLowerCase()) {
            case "order":
            case "logistics":
                // 物流信息 - 使用已有的图标或默认图标
                imageView.setImageResource(R.drawable.awaiting_delivery); // 如果有的话
                break;
            case "promotion":
            case "activity":
                // 活动优惠 - 使用促销图标
                imageView.setImageResource(R.drawable.activity); // 如果有的话
                break;
            case "like":
            case "favorite":
                // 点赞消息 - 使用心形图标
                imageView.setImageResource(R.drawable.like_message); // 如果有的话
                break;
            case "system":
            case "notification":
                // 系统通知 - 使用铃铛图标
                imageView.setImageResource(R.drawable.news); // 如果有的话
                break;
            default:
                // 默认图标
                imageView.setImageResource(R.drawable.news);
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        TextView tvUnreadMark;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_message_icon);
            tvTitle = itemView.findViewById(R.id.tv_message_title);
            tvContent = itemView.findViewById(R.id.tv_message_content);
            tvTime = itemView.findViewById(R.id.tv_message_time);
            tvUnreadMark = itemView.findViewById(R.id.tv_unread_mark);
        }
    }
}