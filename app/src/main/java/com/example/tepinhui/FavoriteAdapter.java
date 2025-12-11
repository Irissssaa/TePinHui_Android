package com.example.tepinhui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteItem> favoriteList;
    private Set<Integer> selectedPositions;
    private boolean isEditMode;

    public FavoriteAdapter(FavoriteActivity favoriteActivity, List<FavoriteItem> favoriteList, boolean isEditMode) {
        this.favoriteList = favoriteList;
        this.isEditMode = isEditMode;
        this.selectedPositions = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FavoriteItem item = favoriteList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvType.setText(item.getType());

        if (item.getCategory().equals("shop")) {
            holder.tvRating.setText(String.format("评分：%.1f", item.getRating()));
            holder.tvRating.setVisibility(View.VISIBLE);
        } else {
            holder.tvRating.setVisibility(View.GONE);
        }

        holder.ivImage.setImageResource(item.getImageResId());

        if (isEditMode) {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setChecked(selectedPositions.contains(position));
        } else {
            holder.cbSelect.setVisibility(View.GONE);
        }

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPositions.add(position);
                } else {
                    selectedPositions.remove(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        if (!editMode) {
            selectedPositions.clear();
        }
        notifyDataSetChanged();
    }

    public List<FavoriteItem> getSelectedItems() {
        List<FavoriteItem> selectedItems = new ArrayList<>();
        for (int position : selectedPositions) {
            selectedItems.add(favoriteList.get(position));
        }
        return selectedItems;
    }

    public void clearSelection() {
        selectedPositions.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        ImageView ivImage;
        TextView tvName;
        TextView tvType;
        TextView tvRating;

        ViewHolder(View view) {
            super(view);
            cbSelect = view.findViewById(R.id.cb_select);
            ivImage = view.findViewById(R.id.iv_image);
            tvName = view.findViewById(R.id.tv_name);
            tvType = view.findViewById(R.id.tv_type);
            tvRating = view.findViewById(R.id.tv_rating);
        }
    }
}