package com.example.tepinhui.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.CartItemDTO;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface Listener {
        void onCheckedChanged(int productId, boolean newChecked, int position, boolean oldChecked);
        void onQuantityChanged(int productId, int newQty, int position, int oldQty);
        void onDeleteClicked(int productId, int position);
    }

    private final Context context;
    private final List<CartItemDTO> list;
    private final Listener listener;

    public CartAdapter(Context context, List<CartItemDTO> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CartItemDTO item = list.get(position);

        h.tvName.setText(item.getName());
        h.tvPrice.setText("¥" + item.getPrice());
        h.tvCount.setText(String.valueOf(item.getQuantity()));
        h.ivImage.setImageResource(R.drawable.ic_image_placeholder);

        // 避免复用导致的多次触发：先置空监听，再 setChecked，再绑监听
        h.cbCheck.setOnCheckedChangeListener(null);
        h.cbCheck.setChecked(item.isChecked());
        h.cbCheck.setOnCheckedChangeListener((btn, isChecked) -> {
            boolean old = item.isChecked();
            if (old == isChecked) return;

            // 先本地更新（乐观更新）
            item.setChecked(isChecked);
            if (listener != null) {
                listener.onCheckedChanged(item.getProductId(), isChecked, h.getBindingAdapterPosition(), old);
            }
        });

        h.btnAdd.setOnClickListener(v -> {
            int old = item.getQuantity();
            int next = old + 1;

            item.setQuantity(next);
            notifyItemChanged(position);

            if (listener != null) {
                listener.onQuantityChanged(item.getProductId(), next, h.getBindingAdapterPosition(), old);
            }
        });

        h.btnMinus.setOnClickListener(v -> {
            int old = item.getQuantity();
            if (old <= 1) return;

            int next = old - 1;

            item.setQuantity(next);
            notifyItemChanged(position);

            if (listener != null) {
                listener.onQuantityChanged(item.getProductId(), next, h.getBindingAdapterPosition(), old);
            }
        });

        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClicked(item.getProductId(), h.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox cbCheck;
        ImageView ivImage;
        TextView tvName, tvPrice, tvCount;
        View btnAdd, btnMinus;
        View btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.cb_check);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
