package com.example.tepinhui.ui.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tepinhui.R;

public class MessageSearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView tvSearchAction;
    private View layoutSearchHistory;
    private View layoutNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_search);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        // 搜索输入框
        etSearch = findViewById(R.id.et_search);

        // 搜索按钮
        tvSearchAction = findViewById(R.id.tv_search_action);

        // 搜索结果区域
        layoutNoResults = findViewById(R.id.layout_no_results);

        // 搜索历史区域
        layoutSearchHistory = findViewById(R.id.layout_search_history);

        // 清空历史按钮
        TextView tvClearHistory = findViewById(R.id.tv_clear_history);
        if (tvClearHistory != null) {
            tvClearHistory.setOnClickListener(v -> {
                Toast.makeText(this, "已清空搜索历史", Toast.LENGTH_SHORT).show();
                // 这里可以添加清空搜索历史的逻辑
            });
        }

        // 搜索历史项点击事件
        setupHistoryItems();

        // 热门搜索标签点击事件
        setupHotSearchTags();
    }

    private void setupListeners() {
        // 搜索按钮点击
        if (tvSearchAction != null) {
            tvSearchAction.setOnClickListener(v -> performSearch());
        }

        // 搜索输入框文本变化监听
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // 实时搜索逻辑（可选）
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void setupHistoryItems() {
        // 历史记录1
        TextView tvHistory1 = findViewById(R.id.tv_history_1);
        if (tvHistory1 != null) {
            tvHistory1.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("物流信息查询");
                    performSearch();
                }
            });
        }

        // 历史记录2
        TextView tvHistory2 = findViewById(R.id.tv_history_2);
        if (tvHistory2 != null) {
            tvHistory2.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("活动优惠券");
                    performSearch();
                }
            });
        }
    }

    private void setupHotSearchTags() {
        // 方法1：直接为每个标签设置点击事件
        setupHotTagsByIndividual();

        // 方法2：通过容器遍历设置点击事件（备用）
        // setupHotTagsByContainer();
    }

    private void setupHotTagsByIndividual() {
        // 物流信息标签
        TextView tvHotLogistics = findViewById(R.id.tv_hot_logistics);
        if (tvHotLogistics != null) {
            tvHotLogistics.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("物流信息");
                    performSearch();
                }
            });
        }

        // 活动优惠标签
        TextView tvHotPromotion = findViewById(R.id.tv_hot_promotion);
        if (tvHotPromotion != null) {
            tvHotPromotion.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("活动优惠");
                    performSearch();
                }
            });
        }

        // 点赞消息标签
        TextView tvHotLike = findViewById(R.id.tv_hot_like);
        if (tvHotLike != null) {
            tvHotLike.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("点赞消息");
                    performSearch();
                }
            });
        }

        // 订单通知标签
        TextView tvHotOrder = findViewById(R.id.tv_hot_order);
        if (tvHotOrder != null) {
            tvHotOrder.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("订单通知");
                    performSearch();
                }
            });
        }

        // 系统通知标签
        TextView tvHotSystem = findViewById(R.id.tv_hot_system);
        if (tvHotSystem != null) {
            tvHotSystem.setOnClickListener(v -> {
                if (etSearch != null) {
                    etSearch.setText("系统通知");
                    performSearch();
                }
            });
        }
    }

    private void setupHotTagsByContainer() {
        // 通过容器遍历设置点击事件
        View container = findViewById(R.id.layout_hot_tags_container);
        if (container instanceof android.widget.LinearLayout) {
            android.widget.LinearLayout linearLayout = (android.widget.LinearLayout) container;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof TextView) {
                    child.setOnClickListener(v -> {
                        String keyword = ((TextView) v).getText().toString();
                        if (etSearch != null) {
                            etSearch.setText(keyword);
                            performSearch();
                        }
                    });
                }
            }
        }
    }

    private void performSearch() {
        if (etSearch != null) {
            String keyword = etSearch.getText().toString().trim();
            if (keyword.isEmpty()) {
                Toast.makeText(this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
                return;
            }

            // 隐藏搜索历史，显示搜索结果区域
            if (layoutSearchHistory != null) {
                layoutSearchHistory.setVisibility(View.GONE);
            }

            // 显示无结果提示（这里模拟无搜索结果）
            if (layoutNoResults != null) {
                layoutNoResults.setVisibility(View.VISIBLE);
            }

            Toast.makeText(this, "搜索: " + keyword, Toast.LENGTH_SHORT).show();

            // 这里可以添加实际的搜索逻辑
            // 例如：调用API搜索消息，然后更新RecyclerView
        }
    }
}