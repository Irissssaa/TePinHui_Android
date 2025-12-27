package com.example.tepinhui.ui.story;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tepinhui.R;
import com.example.tepinhui.dto.StoryDTO;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.VH> {

    private List<StoryDTO> list;
    private Context context;

    public StoryAdapter(Context context, List<StoryDTO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        StoryDTO s = list.get(pos);
        h.tvTitle.setText(s.getTitle());
        Glide.with(context).load(s.getCoverUrl()).into(h.ivCover);

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, StoryDetailActivity.class);
            i.putExtra("storyId", s.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;

        VH(View v) {
            super(v);
            ivCover = v.findViewById(R.id.iv_cover);
            tvTitle = v.findViewById(R.id.tv_title);
        }
    }
}
