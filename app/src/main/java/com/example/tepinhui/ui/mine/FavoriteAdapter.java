package com.example.tepinhui.ui.mine;

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

import com.example.tepinhui.R;
import com.example.tepinhui.dto.ProductDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<ProductDTO> favoriteList;
    private Set<Integer> selectedPositions;
    private Set<Integer> selectedProductIds;
    private boolean isEditMode;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onRemoveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FavoriteAdapter(FavoriteActivity favoriteActivity, List<ProductDTO> favoriteList, boolean isEditMode) {
        this.favoriteList = favoriteList;
        this.isEditMode = isEditMode;
        this.selectedPositions = new HashSet<>();
        this.selectedProductIds = new HashSet<>();
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
        ProductDTO item = favoriteList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvType.setText(item.getOrigin() != null ? item.getOrigin() : "特产");
        holder.tvRating.setText(String.format("¥%.2f", item.getPrice()));

        // 使用 Picasso 加载图片
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_weixin_new) // 默认图片
                    .error(R.drawable.ic_weixin_new)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_weixin_new);
        }

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
                    selectedProductIds.add(item.getId());
                } else {
                    selectedPositions.remove(position);
                    selectedProductIds.remove(item.getId());
                }
            }
        });

        // 商品点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                if (isEditMode) {
                    // 编辑模式下，点击切换选中状态
                    boolean newChecked = !selectedPositions.contains(position);
                    holder.cbSelect.setChecked(newChecked);
                    if (newChecked) {
                        selectedPositions.add(position);
                        selectedProductIds.add(item.getId());
                    } else {
                        selectedPositions.remove(position);
                        selectedProductIds.remove(item.getId());
                    }
                } else {
                    // 非编辑模式下，跳转到商品详情
                    listener.onItemClick(position);
                }
            }
        });

        // 长按进入编辑模式
        holder.itemView.setOnLongClickListener(v -> {
            if (!isEditMode && listener != null) {
                listener.onRemoveClick(position);
                return true;
            }
            return false;
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
            selectedProductIds.clear();
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedProductIds() {
        return new ArrayList<>(selectedProductIds);
    }

    public void clearSelection() {
        selectedPositions.clear();
        selectedProductIds.clear();
        notifyDataSetChanged();
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