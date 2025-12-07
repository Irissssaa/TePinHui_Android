package com.example.tepinhui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
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
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText(String.format("¥ %.2f", product.getPrice()));

        // 使用 Glide 加载图片 (这里使用占位图，实际开发请替换真实URL)
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground) // 默认图
                .into(holder.imgProduct);

        // 购买按钮点击事件
        holder.btnBuy.setOnClickListener(v -> {
            Toast.makeText(context, "已加入购物车: " + product.getName(), Toast.LENGTH_SHORT).show();
            // 这里可以添加跳转到详情页或购物车的逻辑
        });
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
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }
}
