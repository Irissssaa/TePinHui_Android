package com.example.tepinhui.ui.mine;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.AddressDTO;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressDTO> addressList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onSetDefaultClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AddressAdapter(AddressActivity addressActivity, List<AddressDTO> addressList) {
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AddressDTO address = addressList.get(position);

        holder.tvReceiver.setText("收货人: " + address.getReceiver());
        holder.tvPhone.setText(address.getPhone());
        holder.tvAddress.setText(address.getFullAddress());

        if (address.getIsDefault() != null && address.getIsDefault()) {
            holder.cbDefault.setChecked(true);
            holder.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.cbDefault.setChecked(false);
            holder.tvDefault.setVisibility(View.GONE);
        }

        // 设置默认地址勾选监听
        holder.cbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && listener != null) {
                    listener.onSetDefaultClick(position);
                }
            }
        });

        // 编辑按钮点击
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });

        // 删除按钮点击
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });

        // 整个item点击（用于选择地址）
        holder.itemView.setOnClickListener(v -> {
            // 可以用于在订单确认页面选择地址
            // 这里暂时不做处理
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiver, tvPhone, tvAddress, tvDefault;
        CheckBox cbDefault;
        Button btnEdit, btnDelete;

        ViewHolder(View view) {
            super(view);
            tvReceiver = view.findViewById(R.id.tv_receiver);
            tvPhone = view.findViewById(R.id.tv_phone);
            tvAddress = view.findViewById(R.id.tv_address);
            tvDefault = view.findViewById(R.id.tv_default);
            cbDefault = view.findViewById(R.id.cb_default);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnDelete = view.findViewById(R.id.btn_delete);
        }
    }
}