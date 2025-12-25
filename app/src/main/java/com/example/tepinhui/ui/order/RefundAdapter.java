package com.example.tepinhui.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;

import java.util.List;

public class RefundAdapter extends RecyclerView.Adapter<RefundAdapter.ViewHolder> {

    private List<RefundItem> refundList;

    public RefundAdapter(RefundActivity refundActivity, List<RefundItem> refundList) {
        this.refundList = refundList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_refund, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RefundItem item = refundList.get(position);

        holder.tvRefundId.setText("退款单号：" + item.getRefundId());
        holder.tvRefundType.setText(item.getType());
        holder.tvProductName.setText(item.getProductName());
        holder.tvRefundAmount.setText(String.format("¥%.2f", item.getAmount()));

        String statusText = item.getStatus();
        holder.tvRefundStatus.setText(statusText);

        // 根据状态设置颜色
        if ("处理中".equals(statusText)) {
            holder.tvRefundStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
        } else if ("已完成".equals(statusText)) {
            holder.tvRefundStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    @Override
    public int getItemCount() {
        return refundList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRefundId;
        TextView tvRefundType;
        TextView tvProductName;
        TextView tvRefundAmount;
        TextView tvRefundStatus;

        ViewHolder(View view) {
            super(view);
            tvRefundId = view.findViewById(R.id.tv_refund_id);
            tvRefundType = view.findViewById(R.id.tv_refund_type);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvRefundAmount = view.findViewById(R.id.tv_refund_amount);
            tvRefundStatus = view.findViewById(R.id.tv_refund_status);
        }
    }
}