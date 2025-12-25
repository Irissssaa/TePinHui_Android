package com.example.tepinhui.ui.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tepinhui.R;
import com.example.tepinhui.dto.ProductDTO;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductDTO> productList;

    public ProductAdapter(Context context, List<ProductDTO> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDTO product = productList.get(position);

        holder.tvName.setText(product.getName());
        String origin = product.getOrigin();
        holder.tvDescription.setText(
                (origin == null || origin.isEmpty()) ? ("已售 " + product.getSales())
                        : (origin + " · 已售 " + product.getSales())
        );

        holder.tvPrice.setText(String.format("¥ %.2f", product.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            v.getContext().startActivity(intent);
        });


        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgProduct);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder 内部类
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvDescription, tvPrice;
        Button btnBuy;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
