package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;

import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLDetailActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthOrderRefundPriceBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/13 9:53
 */


public class YLOrderDealUtil {
    /**
     * 医养医疗申请退款所退金额
     *
     * @param id    订单id
     * @param state 订单状态
     */
    public static void healthOrderRefundPrice(Activity activity, String id, String state, OnNetRequestSuccessListener listener) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id(必传)
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(ZLURLConstants.HEALTH_ORDER_REFUND_PRICE_URL, map, new OnMyActivityRequestListener<HealthOrderRefundPriceBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(HealthOrderRefundPriceBean bean) {
                showCancelOrderDialog(activity, id, state, false, "确认取消预约？", bean.getData().getContent(), listener);
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                if ("401".equals(code))
                    showCancelOrderDialog(activity, id, state, true, "无法取消预约！", message, listener);
            }
        });

    }

    /**
     * 取消预约弹框
     *
     * @param id       订单id
     * @param state    订单状态
     * @param isOneBtn true:一个按钮 false：两个按钮
     * @param title    弹框标题
     * @param message  弹框内容
     */
    public static void showCancelOrderDialog(Activity activity, String id, String state, boolean isOneBtn, String title, String message, OnNetRequestSuccessListener listener) {
        ZLDialogUtil.showTwoButtonDialog(activity, isOneBtn, "取消", "确定", title, message, new ZLDialogUtil.OnClickCancelConfirmListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                if ("1".equals(state) && !isOneBtn) {
                    // 取消预约
                    healthCancelOrder(activity, id, listener);
                } else if ("2".equals(state)) {
                    // 申请退款
                    healthAddOrderRefund(activity, id, listener);
                }
            }
        });
    }

    /**
     * 申请退款
     *
     * @param id 订单id
     */
    private static void healthAddOrderRefund(Activity activity, String id, OnNetRequestSuccessListener listener) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id(必传)
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        // 用户id
        map.put(URLConstants.REQUEST_PARAM_UID, ZhaoLinApplication.getInstance().getLoginUser().getData().getId());
        RequestManager.createRequest(ZLURLConstants.HEALTH_ADD_ORDER_REFUND_URL, map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(BaseBean bean) {
                if (listener != null)
                    listener.onSuccess(false);
            }

        });

    }

    /**
     * 取消订单（订单状态 state等于1时可显示操作）
     *
     * @param id 订单id
     */
    private static void healthCancelOrder(Activity activity, String id, OnNetRequestSuccessListener listener) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id(必传)
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(ZLURLConstants.HEALTH_CANCEL_ORDER_URL, map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(BaseBean bean) {
                if (listener != null)
                    listener.onSuccess(false);
            }

        });

    }

    /**
     * 删除订单弹框
     *
     * @param id 订单id
     */
    public static void showDeleteOrderDialog(Activity activity, String id, OnNetRequestSuccessListener listener) {
        ZLDialogUtil.showTwoButtonDialog(activity, false, "取消", "确定", "确认删除订单？", "", new ZLDialogUtil.OnClickCancelConfirmListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                healthDelOrder(activity, id, listener);
            }
        });
    }

    /**
     * 删除订单（订单状态 state不等于 1， 2或者3时 显示操作）
     *
     * @param id 订单id
     */
    private static void healthDelOrder(Activity activity, String id, OnNetRequestSuccessListener listener) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id(必传)
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(ZLURLConstants.HEALTH_DEL_ORDER_URL, map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(BaseBean bean) {
                if (listener != null)
                    listener.onSuccess(true);
            }

        });

    }

    /**
     * 支付前检测
     *
     * @param orderId    订单orderId
     * @param orderTitle 订单title
     * @param goodsId    商品id
     */
    public static void checkPay(Activity activity, String orderId, String orderTitle, String goodsId) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id
        map.put(URLConstants.REQUEST_PARAM_ID, orderId);
        RequestManager.createRequest(URLConstants.HEALTH_CHECK_PAY, map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(BaseBean bean) {
                // 跳转到支付界面
                activity.startActivity(PayActivity.newIntent(activity.getApplicationContext(), orderId, orderTitle, 4, ""));
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                // 展示订单失效弹框
                showOrderFailureDialog(activity, goodsId, message);
            }
        });

    }

    /**
     * 订单失效
     *
     * @param goodsId 商品id
     * @param message 提示信息
     */
    private static void showOrderFailureDialog(Activity activity, String goodsId, String message) {
        ZLDialogUtil.showTwoButtonDialog(activity, false, "确定", "再次预约", "无法继续支付！", message, new ZLDialogUtil.OnClickCancelConfirmListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                // 跳转到产品详情
                activity.startActivity(YLDetailActivity.newIntent(activity.getApplicationContext(), goodsId));
            }
        });
    }

    public interface OnNetRequestSuccessListener {
        void onSuccess(boolean isDeleteOrder);
    }
}
