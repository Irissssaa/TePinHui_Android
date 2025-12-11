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

public class FootprintAdapter extends RecyclerView.Adapter<FootprintAdapter.ViewHolder> {

    private List<FootprintItem> footprintList;
    private Set<Integer> selectedPositions;
    private boolean isEditMode;

    public FootprintAdapter(List<FootprintItem> footprintList, boolean isEditMode) {
        this.footprintList = footprintList;
        this.isEditMode = isEditMode;
        this.selectedPositions = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_footprint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FootprintItem item = footprintList.get(position);

        holder.tvProductName.setText(item.getName());
        holder.tvViewTime.setText(item.getViewTime());
        holder.tvPrice.setText(String.format("¥%.2f", item.getPrice()));
        holder.ivProduct.setImageResource(item.getImageResId());

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
        return footprintList.size();
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        if (!editMode) {
            selectedPositions.clear();
        }
        notifyDataSetChanged();
    }

    public List<FootprintItem> getSelectedItems() {
        List<FootprintItem> selectedItems = new ArrayList<>();
        for (int position : selectedPositions) {
            selectedItems.add(footprintList.get(position));
        }
        return selectedItems;
    }

    public void clearSelection() {
        selectedPositions.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvViewTime;
        TextView tvPrice;

        ViewHolder(View view) {
            super(view);
            cbSelect = view.findViewById(R.id.cb_select);
            ivProduct = view.findViewById(R.id.iv_product);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvViewTime = view.findViewById(R.id.tv_view_time);
            tvPrice = view.findViewById(R.id.tv_price);
        }
    }
}