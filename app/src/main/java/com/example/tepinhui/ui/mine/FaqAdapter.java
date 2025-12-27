package com.example.tepinhui.ui.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.R;
import com.example.tepinhui.dto.FaqDTO;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    private List<FaqDTO> faqs;

    public FaqAdapter(List<FaqDTO> faqs) {
        this.faqs = faqs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqDTO faq = faqs.get(position);

        holder.tvQuestion.setText(faq.getQuestion());
        holder.tvAnswer.setText(faq.getAnswer());

        // 点击展开/收起答案
        holder.itemView.setOnClickListener(v -> {
            if (holder.tvAnswer.getVisibility() == View.VISIBLE) {
                holder.tvAnswer.setVisibility(View.GONE);
                holder.tvExpand.setText("展开");
            } else {
                holder.tvAnswer.setVisibility(View.VISIBLE);
                holder.tvExpand.setText("收起");
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvAnswer, tvExpand;

        ViewHolder(View view) {
            super(view);
            tvQuestion = view.findViewById(R.id.tv_question);
            tvAnswer = view.findViewById(R.id.tv_answer);
            tvExpand = view.findViewById(R.id.tv_expand);
        }
    }
}