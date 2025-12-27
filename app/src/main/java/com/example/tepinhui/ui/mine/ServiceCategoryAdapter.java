package com.example.tepinhui.ui.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.ServiceCategoryDTO;

import java.util.List;

public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.ViewHolder> {

    private List<ServiceCategoryDTO> categories;

    public ServiceCategoryAdapter(List<ServiceCategoryDTO> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceCategoryDTO category = categories.get(position);

        holder.tvTitle.setText(category.getName());
        holder.tvDesc.setText(category.getDescription());

        // 加载图标（如果有的话）
        // 这里可以使用Picasso加载网络图片，或者根据category.getIcon()设置本地图标
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvDesc;

        ViewHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.iv_icon);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_desc);
        }
    }
}