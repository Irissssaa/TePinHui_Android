package com.example.tepinhui.ui.story;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tepinhui.R;
import com.example.tepinhui.dto.ProductDTO;
import com.example.tepinhui.ui.product.ProductDetailActivity;

import java.util.List;
import java.util.Locale;

public class StoryProductAdapter
        extends RecyclerView.Adapter<StoryProductAdapter.VH> {

    private final Context context;
    private final List<ProductDTO> list;

    public StoryProductAdapter(Context context, List<ProductDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_story_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ProductDTO p = list.get(pos);

        h.tvName.setText(p.getName());
        h.tvPrice.setText(
                String.format(Locale.CHINA, "¥%.2f", p.getPrice())
        );

        Glide.with(context)
                .load(p.getImageUrl())
                .into(h.ivImg);

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductDetailActivity.class);
            i.putExtra("productId", p.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName, tvPrice;

        VH(@NonNull View v) {
            super(v);
            ivImg = v.findViewById(R.id.iv_img);
            tvName = v.findViewById(R.id.tv_name);
            tvPrice = v.findViewById(R.id.tv_price);
        }
    }
}
