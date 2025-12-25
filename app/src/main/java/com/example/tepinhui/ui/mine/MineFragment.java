package com.example.tepinhui.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tepinhui.ui.order.AllOrdersActivity;
import com.example.tepinhui.ui.service.OnlineServiceActivity;
import com.example.tepinhui.ui.order.PendingPaymentActivity;
import com.example.tepinhui.ui.order.PendingReceiptActivity;
import com.example.tepinhui.ui.order.PendingReviewActivity;
import com.example.tepinhui.ui.order.PendingShipmentActivity;
import com.example.tepinhui.R;
import com.example.tepinhui.ui.order.RefundActivity;

public class MineFragment extends Fragment {

    // 静态工厂方法 (可选，但推荐用于传递参数)
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加载布局
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化并设置所有点击监听器
        initClickListeners(view);
    }

    private void initClickListeners(View view) {
        try {
            // 1. 全部订单
            View layoutAllOrders = view.findViewById(R.id.layout_all_orders);
            if (layoutAllOrders != null) {
                layoutAllOrders.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：全部订单");
                    Toast.makeText(getContext(), "跳转到全部订单", Toast.LENGTH_SHORT).show();
                    navigateToActivity(AllOrdersActivity.class);
                });
            }

            // 2. 待付款
            View layoutPendingPayment = view.findViewById(R.id.layout_pending_payment);
            if (layoutPendingPayment != null) {
                layoutPendingPayment.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待付款");
                    Toast.makeText(getContext(), "跳转到待付款", Toast.LENGTH_SHORT).show();
                    navigateToActivity(PendingPaymentActivity.class);
                });
            }

            // 3. 待发货
            View layoutPendingShipment = view.findViewById(R.id.layout_pending_shipment);
            if (layoutPendingShipment != null) {
                layoutPendingShipment.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待发货");
                    Toast.makeText(getContext(), "跳转到待发货", Toast.LENGTH_SHORT).show();
                    navigateToActivity(PendingShipmentActivity.class);
                });
            }

            // 4. 待收货
            View layoutPendingReceipt = view.findViewById(R.id.layout_pending_receipt);
            if (layoutPendingReceipt != null) {
                layoutPendingReceipt.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待收货");
                    Toast.makeText(getContext(), "跳转到待收货", Toast.LENGTH_SHORT).show();
                    navigateToActivity(PendingReceiptActivity.class);
                });
            }

            // 5. 待评价
            View layoutPendingReview = view.findViewById(R.id.layout_pending_review);
            if (layoutPendingReview != null) {
                layoutPendingReview.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待评价");
                    Toast.makeText(getContext(), "跳转到待评价", Toast.LENGTH_SHORT).show();
                    navigateToActivity(PendingReviewActivity.class);
                });
            }

            // 6. 退款/售后
            View layoutRefund = view.findViewById(R.id.layout_refund);
            if (layoutRefund != null) {
                layoutRefund.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：退款/售后");
                    Toast.makeText(getContext(), "跳转到退款/售后", Toast.LENGTH_SHORT).show();
                    navigateToActivity(RefundActivity.class);
                });
            }

            // 7. 收货地址
            View layoutAddress = view.findViewById(R.id.layout_address);
            if (layoutAddress != null) {
                layoutAddress.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：收货地址");
                    Toast.makeText(getContext(), "跳转到收货地址", Toast.LENGTH_SHORT).show();
                    navigateToActivity(AddressActivity.class);
                });
            }

            // 8. 足迹
            View layoutFootprint = view.findViewById(R.id.layout_footprint);
            if (layoutFootprint != null) {
                layoutFootprint.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：足迹");
                    Toast.makeText(getContext(), "跳转到足迹", Toast.LENGTH_SHORT).show();
                    navigateToActivity(FootprintActivity.class);
                });
            }

            // 9. 我的收藏
            View layoutFavorite = view.findViewById(R.id.layout_favorite);
            if (layoutFavorite != null) {
                layoutFavorite.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：我的收藏");
                    Toast.makeText(getContext(), "跳转到我的收藏", Toast.LENGTH_SHORT).show();
                    navigateToActivity(FavoriteActivity.class);
                });
            }

            // 10. 服务中心
            View layoutServiceCenter = view.findViewById(R.id.layout_service_center);
            if (layoutServiceCenter != null) {
                layoutServiceCenter.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：服务中心");
                    Toast.makeText(getContext(), "跳转到服务中心", Toast.LENGTH_SHORT).show();
                    navigateToActivity(ServiceCenterActivity.class);
                });
            }

            // 11. 在线客服
            View layoutOnlineService = view.findViewById(R.id.layout_online_service);
            if (layoutOnlineService != null) {
                layoutOnlineService.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：在线客服");
                    Toast.makeText(getContext(), "跳转到在线客服", Toast.LENGTH_SHORT).show();
                    navigateToActivity(OnlineServiceActivity.class);
                });
            }
        } catch (Exception e) {
            Log.e("MineFragment", "初始化点击监听器时出错: " + e.getMessage());
            Toast.makeText(getContext(), "功能初始化异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        try {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), activityClass);
                startActivity(intent);
            } else {
                Log.e("MineFragment", "getActivity() 返回 null");
                Toast.makeText(getContext(), "跳转失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MineFragment", "跳转失败: " + e.getMessage());

            // 更详细的错误信息
            if (e instanceof android.content.ActivityNotFoundException) {
                Toast.makeText(getContext(), "目标页面不存在，请检查Activity是否已创建", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}