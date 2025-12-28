package com.example.tepinhui.ui.community;

import android.net.Uri;
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

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.VH> {

    public interface OnRemoveClickListener {
        void onRemove(int position);
    }

    private final List<Uri> uris;
    private OnRemoveClickListener onRemoveClickListener;

    public SelectedImageAdapter(List<Uri> uris) {
        this.uris = uris;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.onRemoveClickListener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_image, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Uri uri = uris.get(position);
        Glide.with(h.ivImage.getContext())
                .load(uri)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(h.ivImage);

        h.btnRemove.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemove(h.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uris == null ? 0 : uris.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView btnRemove;

        VH(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}


