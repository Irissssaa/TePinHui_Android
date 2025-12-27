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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FootprintAdapter extends RecyclerView.Adapter<FootprintAdapter.ViewHolder> {

    private List<ProductDTO> footprintList;
    private Set<Integer> selectedPositions;
    private boolean isEditMode;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onRemoveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FootprintAdapter(List<ProductDTO> footprintList, boolean isEditMode) {
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
        ProductDTO item = footprintList.get(position);

        holder.tvProductName.setText(item.getName());
        holder.tvPrice.setText(String.format("¥%.2f", item.getPrice()));

        // 显示浏览时间（这里简化处理，实际应该从足迹记录中获取时间）
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        String viewTime = sdf.format(new Date());
        holder.tvViewTime.setText(viewTime);

        // 加载图片
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_weixin_new)
                    .error(R.drawable.ic_weixin_new)
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.drawable.ic_weixin_new);
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
                } else {
                    selectedPositions.remove(position);
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
                    } else {
                        selectedPositions.remove(position);
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
        return footprintList.size();
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        if (!editMode) {
            selectedPositions.clear();
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(selectedPositions);
    }

    public void clearSelection() {
        selectedPositions.clear();
        notifyDataSetChanged();
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