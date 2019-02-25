package com.xxzlkj.shop.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.CancelOrderGroupBean;
import com.xxzlkj.shop.model.CleaningTuiDescBean;
import com.xxzlkj.shop.model.RefundReasonBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.qqtheme.framework.picker.OptionPicker;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/23 14:46
 */
public class OrderUtils {
    /**
     * 设置倒计时
     */
    public static void setCountDownTimer(final Context mContext, final BaseViewHolder holder, final TextView textView, String nowTimeStr, final String endTimeStr) {
        textView.setVisibility(View.VISIBLE);
        long endTime = NumberFormatUtils.toLong(endTimeStr) * 1000;
        long nowTime = NumberFormatUtils.toLong(nowTimeStr) * 1000;
        if (endTime == 0 || nowTime == 0) {
            textView.setText("——");
        }
        CountDownTimer countDownTimer;
        if (endTime > nowTime) {
            // 正常，未超时
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.orange_f6a623));
            long countTime = endTime - nowTime;
            countDownTimer = new CountDownTimer(countTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // 转换为秒
                    long secondsValue = (millisUntilFinished + 500) / 1000;//换成秒,4舍5入
                    long mm = secondsValue / 60;
                    long ss = (secondsValue - mm * 60);
                    textView.setText(String.format(Locale.CANADA, "倒计时：%02d分%02d秒", mm, ss));
                }

                @Override
                public void onFinish() {
                    cancel();
                    setCountDownTimer(mContext, holder, textView, endTimeStr, endTimeStr);
                }
            };


        } else {
            // 超时
            final long countTime = nowTime - endTime;
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff725c));
            countDownTimer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
                long secondsValue = countTime / 1000;

                @Override
                public void onTick(long millisUntilFinished) {
                    // 转换为秒
                    ++secondsValue;
                    long mm = secondsValue / 60;
                    long ss = (secondsValue - mm * 60);
                    textView.setText(String.format(Locale.CANADA, "已超时：%02d分%02d秒", mm, ss));
                }

                @Override
                public void onFinish() {

                }
            };
        }
        countDownTimer.start();
        if (holder != null)
            holder.setTag(countDownTimer);
    }

    public static void setTime(Context mContext, TextView textView, String finishTimeStr, String endTimeStr) {
        textView.setVisibility(View.VISIBLE);
//        简化：finishtime-endtime>0 超时，<0正常耗时
        long endTime = NumberFormatUtils.toLong(endTimeStr) * 1000;
        long finishTime = NumberFormatUtils.toLong(finishTimeStr) * 1000;
        if (endTime == 0 || finishTime == 0) {
            textView.setText("——");
            return;
        }
        long time = Math.abs(finishTime - endTime);
        // 转换为秒
        long secondsValue = (time + 500) / 1000;//换成秒,4舍5入
        long mm = (secondsValue) / 60;
        long ss = (secondsValue - mm * 60);
        if (finishTime > endTime) {
            // 超时
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff725c));
            textView.setText(String.format(Locale.CANADA, "已超时：%02d分%02d秒", mm, ss));
        } else {
            // 正常耗时
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black_777777));
            textView.setText(String.format(Locale.CANADA, "已耗时：%02d分%02d秒", mm, ss));
        }
    }

    /**
     * 退货详情设置倒计时
     */
    public static void setRefundCountDownTimer(final TextView textView, BaseViewHolder holder, String nowTimeStr, final String endTimeStr, final String stateTitle) {
        textView.setVisibility(View.VISIBLE);
        long endTime = NumberFormatUtils.toLong(endTimeStr) * 1000;
        long nowTime = NumberFormatUtils.toLong(nowTimeStr) * 1000;
        LogUtil.e("nowTime", DateUtils.getYearMonthDayHourMinuteSeconds(nowTime));
        LogUtil.e("endTime", DateUtils.getYearMonthDayHourMinuteSeconds(endTime));
        if (endTime == 0 || nowTime == 0) {
            textView.setText("——");
        }
        CountDownTimer countDownTimer;
        long countTime = endTime - nowTime;
        countDownTimer = new CountDownTimer(countTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 转换为秒
                long secondsValue = (millisUntilFinished + 500) / 1000;//换成秒,4舍5入


                long HH = secondsValue / 3600 / 24;
                long mm = (secondsValue - HH * 3600 * 24) / 3600;
                long ss = (secondsValue - HH * 3600 * 24 - mm * 3600) / 60;
                textView.setText(String.format("%02d天%02d小时%02d分钟后" + stateTitle, HH, mm, ss));
            }

            @Override
            public void onFinish() {
                textView.setText("已超时");
            }
        }.start();
        if (holder != null)
            holder.setTag(countDownTimer);
    }

    /**
     * 取消订单接口--- 带弹框
     */
    public static void submitCancelOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.CANCEL_ORDER_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<CancelOrderGroupBean>(activity) {

            @Override
            public void onSuccess(CancelOrderGroupBean bean) {
                OptionPicker picker = new OptionPicker(activity, bean.getData().getGroup());
                picker.setTitleText("请选择原因");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        submitCancelOrder(activity, orderId, item, onSuccessListener);
                    }
                });
                picker.show();
            }
        });
    }

    /**
     * 取消订单接口
     */
    private static void submitCancelOrder(final BaseActivity activity, String orderId, String content, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", orderId);
        stringStringHashMap.put("content", content);
        RequestManager.createRequest(URLConstants.CANCEL_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess();
                }
            }
        });
    }

    /**
     * 删除订单
     */
    public static void submitDelOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "确认删除订单？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.DEL_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 确认收货
     */
    public static void submitFinishOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "是否确认收货？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.FINISH_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 取消申请退款
     */
    public static void submitCancelOrderRefundHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "撤销申请后您将不能重新发起售后申请，是否确认撤销？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.CANCEL_ORDER_REFUND_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 选择退款原因--- 带弹框
     */
    public static void defRefundReason(final BaseActivity activity, final TextView textView, boolean isLocalLife) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(isLocalLife ? URLConstants.JZ_REQUEST_ORDER_REFUND_GROUP : URLConstants.REQUEST_ORDER_REFUND_GROUP, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(activity) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                List<String> group = bean.getData().getGroup();
                if (group != null && group.size() > 0) {
                    textView.setText(group.get(0));
                }
            }
        });
    }

    /**
     * 选择申请售后分类--- 带弹框
     */
    public static void defRefundGroup(final BaseActivity activity, final TextView textView, boolean isLocalLife) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(isLocalLife ? URLConstants.JZ_REFUND_GROUP_URL : URLConstants.JZ_REFUND_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(activity) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                List<String> group = bean.getData().getGroup();
                if (group != null && group.size() > 0) {
                    textView.setText(group.get(0));
                }
            }
        });
    }

    /**
     * 选择退款原因--- 带弹框
     */
    public static void selectRefundReasonHasDialog(final BaseActivity activity, final TextView textView, boolean isLocalLife) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(isLocalLife ? URLConstants.JZ_REQUEST_ORDER_REFUND_GROUP : URLConstants.REQUEST_ORDER_REFUND_GROUP, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(activity) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                OptionPicker picker = new OptionPicker(activity, bean.getData().getGroup());
                picker.setTitleText("请选择退款原因");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        textView.setText(item);
                    }
                });
                picker.show();
            }
        });
    }

    /**
     * 选择申请售后分类--- 带弹框
     */
    public static void selectRefundGroupHasDialog(final BaseActivity activity, final TextView textView, boolean isLocalLife) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(isLocalLife ? URLConstants.JZ_REFUND_GROUP_URL : URLConstants.JZ_REFUND_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(activity) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                OptionPicker picker = new OptionPicker(activity, bean.getData().getGroup());
                picker.setTitleText("请选择退款原因");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        textView.setText(item);
                    }
                });
                picker.show();
            }
        });
    }

    /**
     * 本地生活---取消订单接口--- 带弹框
     */
    public static void submitLocalLifeCancelOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.JZ_CANCEL_ORDER_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<CancelOrderGroupBean>(activity) {

            @Override
            public void onSuccess(CancelOrderGroupBean bean) {
                OptionPicker picker = new OptionPicker(activity, bean.getData().getGroup());
                picker.setTitleText("请选择原因");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        submitLocalLifeCancelOrder(activity, orderId, item, onSuccessListener);
                    }
                });
                picker.show();
            }
        });
    }

    /**
     * 本地生活---取消订单接口
     */
    public static void submitLocalLifeCancelOrder(final BaseActivity activity, String orderId, String content, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", orderId);
        stringStringHashMap.put("content", content);
        RequestManager.createRequest(URLConstants.JZ_CANCEL_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess();
                }
            }
        });
    }

    /**
     * 本地生活---服务完成接口
     */
    public static void submitLocalLifeServiceFinishHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "确认后服务金额将支付给商家，您确定进行此操作？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.JZ_FINISH_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 本地生活--- 删除订单
     */
    public static void submitLocalLifeDelOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "确认删除订单？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.JZ_DEL_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 本地生活--- 申请退款
     */
    public static void submitLocalLifeApplyRefundHasDialog(final BaseActivity activity, String orderId, final ZLDialogUtil.OnClickConfirmListener onClickConfirmListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_ID, orderId);
        RequestManager.createRequest(URLConstants.JZ_CLEANING_TUI_DESC_URL, stringStringHashMap, new OnMyActivityRequestListener<CleaningTuiDescBean>(activity) {

            @Override
            public void onSuccess(CleaningTuiDescBean bean) {
                ZLDialogUtil.showRawDialog(activity, bean.getData().getDesc(), onClickConfirmListener);
            }

            @Override
            public void onError(String code, String message) {
//                super.onError(code, message);
                // 400 直接跳转，不用提示
                if (onClickConfirmListener != null)
                    onClickConfirmListener.onClickConfirm();
            }

        });

    }

    /**
     * 本地生活--- 取消申请退款
     */
    public static void submitLocalLifeCancelOrderRefundHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "撤销申请后您将不能重新发起售后申请，是否确认撤销？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.JZ_CANCEL_ORDER_REFUND_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    /**
     * 本地生活--- 取消申请售后
     */
    public static void submitLocalLifeCancelRefundGoodsHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        ZLDialogUtil.showRawDialog(activity, "撤销申请后您将不能重新发起售后申请，是否确认撤销？", new ZLDialogUtil.OnClickConfirmListener() {
            @Override
            public void onClickConfirm() {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("id", orderId);
                RequestManager.createRequest(URLConstants.JZ_CANCEL_REFUND_GOODS, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess();
                        }
                    }
                });
            }
        });
    }

    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnAfterSaleSuccessListener {
        void onSuccess();
    }
}
