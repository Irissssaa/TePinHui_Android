package com.example.tepinhui.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.tepinhui.ui.auth.LoginActivity;
import com.example.tepinhui.dto.OrderStatsDTO;
import com.example.tepinhui.dto.UserDTO;
import com.example.tepinhui.network.UserApiService;
import com.example.tepinhui.network.OrderApiService;
import com.example.tepinhui.Result;
import com.example.tepinhui.NetworkUtils;

import java.util.Map;

public class MineFragment extends Fragment {

    // 静态工厂方法
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    private TextView tvUsername, tvUserId;
    private TextView tvOrderUnpaid, tvOrderUnship, tvOrderShipped, tvOrderUnreview, tvOrderRefund;
    private View layoutUserInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initClickListeners(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次回到页面时刷新数据
        loadUserProfile();
    }

    private void initViews(View view) {
        try {
            // 用户信息区域
            layoutUserInfo = view.findViewById(R.id.layout_user_info);
            tvUsername = view.findViewById(R.id.tv_username);
            tvUserId = view.findViewById(R.id.tv_user_id); // 使用tv_user_id而不是tv_phone

            // 由于布局中没有显示订单数量的TextView，我们先不使用这些变量
            // 如果你需要显示订单数量，需要在布局中添加对应的TextView
            // tvOrderUnpaid = view.findViewById(R.id.tv_order_unpaid);
            // tvOrderUnship = view.findViewById(R.id.tv_order_unship);
            // tvOrderShipped = view.findViewById(R.id.tv_order_shipped);
            // tvOrderUnreview = view.findViewById(R.id.tv_order_unreview);
            // tvOrderRefund = view.findViewById(R.id.tv_order_refund);

            // 加载用户数据
            loadUserProfile();
        } catch (Exception e) {
            Log.e("MineFragment", "初始化视图出错: " + e.getMessage());
        }
    }

    private void initClickListeners(View view) {
        try {
            // 1. 用户信息区域点击事件（登录/个人资料）
            if (layoutUserInfo != null) {
                layoutUserInfo.setOnClickListener(v -> {
                    if (UserApiService.isLoggedIn(requireContext())) {
                        // 已登录，跳转到个人资料页面
                        Toast.makeText(getContext(), "查看个人资料", Toast.LENGTH_SHORT).show();
                        // navigateToActivity(UserProfileActivity.class);
                    } else {
                        // 未登录，跳转到登录页面
                        navigateToActivity(LoginActivity.class);
                    }
                });
            }

            // 2. 全部订单
            View layoutAllOrders = view.findViewById(R.id.layout_all_orders);
            if (layoutAllOrders != null) {
                layoutAllOrders.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：全部订单");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到全部订单", Toast.LENGTH_SHORT).show();
                        navigateToActivity(AllOrdersActivity.class);
                    }
                });
            }

            // 3. 待付款
            View layoutPendingPayment = view.findViewById(R.id.layout_pending_payment);
            if (layoutPendingPayment != null) {
                layoutPendingPayment.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待付款");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到待付款", Toast.LENGTH_SHORT).show();
                        navigateToActivity(PendingPaymentActivity.class);
                    }
                });
            }

            // 4. 待发货
            View layoutPendingShipment = view.findViewById(R.id.layout_pending_shipment);
            if (layoutPendingShipment != null) {
                layoutPendingShipment.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待发货");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到待发货", Toast.LENGTH_SHORT).show();
                        navigateToActivity(PendingShipmentActivity.class);
                    }
                });
            }

            // 5. 待收货
            View layoutPendingReceipt = view.findViewById(R.id.layout_pending_receipt);
            if (layoutPendingReceipt != null) {
                layoutPendingReceipt.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待收货");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到待收货", Toast.LENGTH_SHORT).show();
                        navigateToActivity(PendingReceiptActivity.class);
                    }
                });
            }

            // 6. 待评价
            View layoutPendingReview = view.findViewById(R.id.layout_pending_review);
            if (layoutPendingReview != null) {
                layoutPendingReview.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：待评价");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到待评价", Toast.LENGTH_SHORT).show();
                        navigateToActivity(PendingReviewActivity.class);
                    }
                });
            }

            // 7. 退款/售后
            View layoutRefund = view.findViewById(R.id.layout_refund);
            if (layoutRefund != null) {
                layoutRefund.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：退款/售后");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到退款/售后", Toast.LENGTH_SHORT).show();
                        navigateToActivity(RefundActivity.class);
                    }
                });
            }

            // 8. 收货地址
            View layoutAddress = view.findViewById(R.id.layout_address);
            if (layoutAddress != null) {
                layoutAddress.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：收货地址");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到收货地址", Toast.LENGTH_SHORT).show();
                        navigateToActivity(AddressActivity.class);
                    }
                });
            }

            // 9. 足迹
            View layoutFootprint = view.findViewById(R.id.layout_footprint);
            if (layoutFootprint != null) {
                layoutFootprint.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：足迹");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到足迹", Toast.LENGTH_SHORT).show();
                        navigateToActivity(FootprintActivity.class);
                    }
                });
            }

            // 10. 我的收藏
            View layoutFavorite = view.findViewById(R.id.layout_favorite);
            if (layoutFavorite != null) {
                layoutFavorite.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：我的收藏");
                    if (checkLogin()) {
                        Toast.makeText(getContext(), "跳转到我的收藏", Toast.LENGTH_SHORT).show();
                        navigateToActivity(FavoriteActivity.class);
                    }
                });
            }

            // 11. 服务中心
            View layoutServiceCenter = view.findViewById(R.id.layout_service_center);
            if (layoutServiceCenter != null) {
                layoutServiceCenter.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：服务中心");
                    Toast.makeText(getContext(), "跳转到服务中心", Toast.LENGTH_SHORT).show();
                    navigateToActivity(ServiceCenterActivity.class);
                });
            }

            // 12. 在线客服
            View layoutOnlineService = view.findViewById(R.id.layout_online_service);
            if (layoutOnlineService != null) {
                layoutOnlineService.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：在线客服");
                    Toast.makeText(getContext(), "跳转到在线客服", Toast.LENGTH_SHORT).show();
                    navigateToActivity(OnlineServiceActivity.class);
                });
            }

            // 13. 推荐商品点击
            View layoutProduct1 = view.findViewById(R.id.layout_product_1);
            if (layoutProduct1 != null) {
                layoutProduct1.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "查看推荐商品1", Toast.LENGTH_SHORT).show();
                });
            }

            View layoutProduct2 = view.findViewById(R.id.layout_product_2);
            if (layoutProduct2 != null) {
                layoutProduct2.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "查看推荐商品2", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e("MineFragment", "初始化点击监听器时出错: " + e.getMessage());
            Toast.makeText(getContext(), "功能初始化异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile() {
        if (!UserApiService.isLoggedIn(requireContext())) {
            showLoginUI();
            return;
        }

        UserApiService.getProfile(requireContext(), new NetworkUtils.Callback<Result<Map<String, Object>>>() {
            @Override
            public void onSuccess(Result<Map<String, Object>> result) {
                if (result != null && result.isSuccess() && result.getData() != null) {
                    updateProfileUI(result.getData());
                } else {
                    Toast.makeText(requireContext(),
                            "加载失败: " + (result != null ? result.getMsg() : "未知错误"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(requireContext(), "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                showLoginUI(); // 如果网络错误，也显示登录UI
            }
        });
    }

    private void updateProfileUI(Map<String, Object> profileData) {
        try {
            // 更新用户信息
            UserDTO user = UserApiService.parseUserInfo(profileData);
            if (user != null) {
                tvUsername.setText(user.getUsername() != null ? user.getUsername() : "用户");
                // 显示手机号或用户ID
                String displayText = user.getPhone() != null ? user.getPhone() :
                        (user.getId() != null ? "ID: " + user.getId() : "");
                tvUserId.setText(displayText);
            } else {
                tvUsername.setText("用户");
                tvUserId.setText("");
            }

            // 更新订单统计 - 注意：如果布局中没有这些TextView，请注释掉这部分
            // OrderStatsDTO orderStats = UserApiService.parseOrderStats(profileData);
            // if (orderStats != null) {
            //     if (tvOrderUnpaid != null) tvOrderUnpaid.setText(String.valueOf(orderStats.getUnpaid()));
            //     if (tvOrderUnship != null) tvOrderUnship.setText(String.valueOf(orderStats.getUnship()));
            //     if (tvOrderShipped != null) tvOrderShipped.setText(String.valueOf(orderStats.getShipped()));
            //     if (tvOrderUnreview != null) tvOrderUnreview.setText(String.valueOf(orderStats.getUnreview()));
            //     if (tvOrderRefund != null) tvOrderRefund.setText(String.valueOf(orderStats.getRefund()));
            // } else {
            //     if (tvOrderUnpaid != null) tvOrderUnpaid.setText("0");
            //     if (tvOrderUnship != null) tvOrderUnship.setText("0");
            //     if (tvOrderShipped != null) tvOrderShipped.setText("0");
            //     if (tvOrderUnreview != null) tvOrderUnreview.setText("0");
            //     if (tvOrderRefund != null) tvOrderRefund.setText("0");
            // }

            // 更新未读消息数量
            long unreadCount = UserApiService.getUnreadMessageCount(profileData);
            if (unreadCount > 0) {
                // 显示消息红点
                // updateMessageBadge(unreadCount);
            }
        } catch (Exception e) {
            Log.e("MineFragment", "更新UI出错: " + e.getMessage());
        }
    }

    private void showLoginUI() {
        if (tvUsername != null) {
            tvUsername.setText("点击登录");
        }
        if (tvUserId != null) {
            tvUserId.setText("登录后查看更多");
        }

        // 重置订单数量 - 如果使用了订单数量TextView，需要重置
        // if (tvOrderUnpaid != null) tvOrderUnpaid.setText("0");
        // if (tvOrderUnship != null) tvOrderUnship.setText("0");
        // if (tvOrderShipped != null) tvOrderShipped.setText("0");
        // if (tvOrderUnreview != null) tvOrderUnreview.setText("0");
        // if (tvOrderRefund != null) tvOrderRefund.setText("0");
    }

    private boolean checkLogin() {
        if (!UserApiService.isLoggedIn(requireContext())) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            navigateToActivity(LoginActivity.class);
            return false;
        }
        return true;
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
            if (e instanceof android.content.ActivityNotFoundException) {
                Toast.makeText(getContext(), "目标页面不存在，请检查Activity是否已创建", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}