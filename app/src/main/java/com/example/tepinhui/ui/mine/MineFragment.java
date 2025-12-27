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
import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        Log.d("MineFragment", "onCreateView: 创建视图");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("MineFragment", "onViewCreated: 视图已创建");

        initViews(view);
        initClickListeners(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MineFragment", "onResume: 刷新数据");
        // 每次回到页面时刷新数据
        loadUserProfile();
    }

    private void initViews(View view) {
        try {
            // 用户信息区域
            layoutUserInfo = view.findViewById(R.id.layout_user_info);
            tvUsername = view.findViewById(R.id.tv_username);
            tvUserId = view.findViewById(R.id.tv_user_id);

            Log.d("MineFragment", "初始化视图完成");

            // 加载用户数据
            loadUserProfile();
        } catch (Exception e) {
            Log.e("MineFragment", "初始化视图出错: " + e.getMessage(), e);
        }
    }

    private void initClickListeners(View view) {
        try {
            // 1. 用户信息区域点击事件（登录/个人资料）
            if (layoutUserInfo != null) {
                layoutUserInfo.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击用户信息区域");
                    if (UserApiService.isLoggedIn(requireContext())) {
                        // 已登录，显示退出登录对话框
                        showLogoutDialog();
                    } else {
                        // 未登录，跳转到登录页面
                        Log.d("MineFragment", "用户未登录，跳转到登录页面");
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

            // 13. 退出登录按钮
            View layoutLogout = view.findViewById(R.id.layout_logout);
            if (layoutLogout != null) {
                layoutLogout.setOnClickListener(v -> {
                    Log.d("MineFragment", "点击：退出登录");
                    if (UserApiService.isLoggedIn(requireContext())) {
                        showLogoutDialog();
                    } else {
                        Toast.makeText(getContext(), "您尚未登录", Toast.LENGTH_SHORT).show();
                        navigateToActivity(LoginActivity.class);
                    }
                });
            }

        } catch (Exception e) {
            Log.e("MineFragment", "初始化点击监听器时出错: " + e.getMessage(), e);
            Toast.makeText(getContext(), "功能初始化异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutDialog() {
        if (getActivity() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle("退出登录")
                .setMessage("确定要退出当前账号吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    logout();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 执行退出登录操作
     */
    private void logout() {
        Log.d("MineFragment", "开始退出登录");

        UserApiService.logout(requireContext(), new NetworkUtils.Callback<Result<Void>>() {
            @Override
            public void onSuccess(Result<Void> result) {
                Log.d("MineFragment", "退出登录成功: " + result.getMsg());

                // 更新UI显示
                showLoginUI();

                // 显示提示
                if (getContext() != null) {
                    Toast.makeText(getContext(), "退出登录成功", Toast.LENGTH_SHORT).show();
                }

                // 可选：跳转到登录页面
                // navigateToActivity(LoginActivity.class);

                // 可选：刷新页面数据
                loadUserProfile();
            }

            @Override
            public void onError(String msg) {
                Log.e("MineFragment", "退出登录失败: " + msg);

                // 即使网络请求失败，本地数据也会被清除，所以还是要更新UI
                showLoginUI();

                if (getContext() != null) {
                    Toast.makeText(getContext(), "已清除本地登录状态", Toast.LENGTH_SHORT).show();
                }

                // 刷新页面数据
                loadUserProfile();
            }
        });
    }

    private void loadUserProfile() {
        Log.d("MineFragment", "loadUserProfile: 开始加载用户信息");

        // 先检查登录状态
        boolean isLoggedIn = UserApiService.isLoggedIn(requireContext());
        Log.d("MineFragment", "登录状态: " + (isLoggedIn ? "已登录" : "未登录"));

        if (!isLoggedIn) {
            showLoginUI();
            return;
        }

        // 显示加载中的状态
        tvUsername.setText("加载中...");
        tvUserId.setText("正在获取用户信息...");

        UserApiService.getProfile(requireContext(), new NetworkUtils.Callback<Result<Map<String, Object>>>() {
            @Override
            public void onSuccess(Result<Map<String, Object>> result) {
                Log.d("MineFragment", "获取用户信息成功: " + (result != null ? result.getMsg() : "null"));

                if (result != null && result.isSuccess() && result.getData() != null) {
                    updateProfileUI(result.getData());
                } else {
                    String errorMsg = result != null ? result.getMsg() : "未知错误";
                    Log.e("MineFragment", "获取用户信息失败: " + errorMsg);

                    Toast.makeText(requireContext(),
                            "加载失败: " + errorMsg,
                            Toast.LENGTH_SHORT).show();

                    // 如果token失效，清除登录状态
                    if ("无效的token".equals(errorMsg) || "请先登录".equals(errorMsg)) {
                        Log.d("MineFragment", "token失效，清除登录状态");
                        UserApiService.clearUserInfo(requireContext());
                        showLoginUI();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                Log.e("MineFragment", "网络错误: " + msg);
                Toast.makeText(requireContext(), "网络错误: " + msg, Toast.LENGTH_SHORT).show();
                showLoginUI(); // 如果网络错误，也显示登录UI
            }
        });
    }

    private void updateProfileUI(Map<String, Object> profileData) {
        try {
            Log.d("MineFragment", "updateProfileUI: 更新UI，数据大小: " + profileData.size());

            // 更新用户信息
            UserDTO user = UserApiService.parseUserInfo(profileData);
            if (user != null) {
                String username = user.getUsername() != null ? user.getUsername() : "用户";
                tvUsername.setText(username);

                // 显示手机号或用户ID
                String displayText = user.getPhone() != null ? user.getPhone() :
                        (user.getId() != null ? "ID: " + user.getId() : "");
                tvUserId.setText(displayText);

                Log.d("MineFragment", "用户信息: " + username + ", " + displayText);
            } else {
                tvUsername.setText("用户");
                tvUserId.setText("");
                Log.e("MineFragment", "解析用户信息失败，user为null");
            }

            // 更新未读消息数量
            long unreadCount = UserApiService.getUnreadMessageCount(profileData);
            Log.d("MineFragment", "未读消息数量: " + unreadCount);

        } catch (Exception e) {
            Log.e("MineFragment", "更新UI出错: " + e.getMessage(), e);
        }
    }

    private void showLoginUI() {
        Log.d("MineFragment", "showLoginUI: 显示登录UI");
        if (tvUsername != null) {
            tvUsername.setText("点击登录");
        }
        if (tvUserId != null) {
            tvUserId.setText("登录后查看更多");
        }
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
            Log.e("MineFragment", "跳转失败: " + e.getMessage(), e);
            if (e instanceof android.content.ActivityNotFoundException) {
                Toast.makeText(getContext(), "目标页面不存在，请检查Activity是否已创建", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}