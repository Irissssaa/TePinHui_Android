package com.example.tepinhui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderItem> orderList;
    private Set<Integer> selectedPositions;
    private boolean isEditMode;

    public OrderAdapter(AllOrdersActivity allOrdersActivity, List<OrderItem> orderList, boolean isEditMode) {
        this.orderList = orderList;
        this.isEditMode = isEditMode;
        this.selectedPositions = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderItem item = orderList.get(position);

        holder.tvOrderId.setText("订单号：" + item.getOrderId());
        holder.tvOrderStatus.setText(item.getStatus());
        holder.tvProductName.setText(item.getProductName());
        holder.tvOrderAmount.setText(String.format("¥%.2f", item.getAmount()));
        holder.tvOrderDate.setText("下单时间：" + item.getDate());

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
        return orderList.size();
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        if (!editMode) {
            selectedPositions.clear();
        }
        notifyDataSetChanged();
    }

    public List<OrderItem> getSelectedOrders() {
        List<OrderItem> selectedItems = new ArrayList<>();
        for (int position : selectedPositions) {
            selectedItems.add(orderList.get(position));
        }
        return selectedItems;
    }

    public void clearSelection() {
        selectedPositions.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        TextView tvOrderId;
        TextView tvOrderStatus;
        TextView tvProductName;
        TextView tvOrderAmount;
        TextView tvOrderDate;

        ViewHolder(View view) {
            super(view);
            cbSelect = view.findViewById(R.id.cb_select);
            tvOrderId = view.findViewById(R.id.tv_order_id);
            tvOrderStatus = view.findViewById(R.id.tv_order_status);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvOrderAmount = view.findViewById(R.id.tv_order_amount);
            tvOrderDate = view.findViewById(R.id.tv_order_date);
        }
    }
}