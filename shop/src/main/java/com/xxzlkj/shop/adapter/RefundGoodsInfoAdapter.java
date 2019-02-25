package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shopOrder.ApplySalesActivity;
import com.xxzlkj.shop.activity.shopOrder.SalesReturnWayActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AfterSaleDetailEvent;
import com.xxzlkj.shop.model.RefundDetail;
import com.xxzlkj.shop.model.RefundGoods;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * 售后详情
 * Created by Administrator on 2017/4/11.
 */

public class RefundGoodsInfoAdapter extends BaseAdapter<RefundDetail.RefundDataBean> {
    private Activity mActivity;
    private String id;
    private RefundGoods.DataBean dataBean;

    public RefundGoodsInfoAdapter(Context context, Activity mActivity, int itemId, RefundGoods.DataBean dataBean) {
        super(context, itemId);
        this.mActivity = mActivity;
        if (dataBean != null){
            this.dataBean = dataBean;
        }
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final RefundDetail.RefundDataBean itemBean) {
        //四种样式
        LinearLayout mLayout1 = holder.getView(R.id.id_rli_layout_1);
        LinearLayout mLayout2 = holder.getView(R.id.id_rli_layout_2);
        LinearLayout mLayout3 = holder.getView(R.id.id_rli_layout_3);
        LinearLayout mLayout5 = holder.getView(R.id.id_rli_layout_5);
        //左右用户名
        TextView mLeftUserName = holder.getView(R.id.id_rli_user_left);
        TextView mRightUserName = holder.getView(R.id.id_rli_user_right);
        RelativeLayout mMainLayout = holder.getView(R.id.id_rli_main_layout);
        String classType = itemBean.getClass_type();
        if ("1".equals(classType)) {
            mRightUserName.setText(itemBean.getUsername());
            mRightUserName.setVisibility(View.VISIBLE);
            mLeftUserName.setVisibility(View.GONE);
            mMainLayout.setBackgroundResource(R.drawable.bg_refund_des_right);
        } else {
            mLeftUserName.setText(itemBean.getUsername());
            mLeftUserName.setVisibility(View.VISIBLE);
            mRightUserName.setVisibility(View.GONE);
            mMainLayout.setBackgroundResource(R.drawable.bg_refund_des_left);
        }
        //隐藏所有
        mLayout1.setVisibility(View.GONE);
        mLayout2.setVisibility(View.GONE);
        mLayout3.setVisibility(View.GONE);
        mLayout5.setVisibility(View.GONE);

        int style = NumberFormatUtils.toInt(itemBean.getStyle());
        switch (style) {
            case 1:
                id = itemBean.getId();
                mLayout1.setVisibility(View.VISIBLE);
                LinearLayout mLinearLayout1 = holder.getView(R.id.id_layout_1_content);
                setContentBg(mLinearLayout1, classType);
                //状态
                TextView mStateTextView1 = holder.getTextView(R.id.id_layout_1_state);
                mStateTextView1.setText(itemBean.getTitle());
                //退款类型
                TextView mTuiModeTextView = holder.getView(R.id.id_layout_1_type);
                String mTuiMode = itemBean.getTui_mode();
                String mTuiModeStr;
                if ("1".equals(mTuiMode)) {
                    mTuiModeStr = "退款退货";
                } else {
                    mTuiModeStr = "退款不退货";
                }
                mTuiModeTextView.setText(mTuiModeStr);

                //申请时间
                TextView mAddTimeTextView = holder.getView(R.id.id_layout_1_time);
                String mAddtime = itemBean.getAddtime();
                String mYearMonthDayHourMinuteSeconds = DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(mAddtime) * 1000);
                mAddTimeTextView.setText(mYearMonthDayHourMinuteSeconds);
                //详细原因/退货说明
                final String mContent = itemBean.getContent();
                TextView mIntroductionTextView = holder.getView(R.id.id_layout_1_introduction);
                TextViewUtils.setText(mIntroductionTextView, mContent);
                //退款金额
                String mPrice = itemBean.getPrice();
                TextView mPriceTextView = holder.getView(R.id.id_layout_1_money);
                mPriceTextView.setText("￥" + mPrice);
                //退款原因
                String mTitle = itemBean.getTitle();
                TextView mReasonTextView = holder.getView(R.id.id_layout_1_reason);
                mReasonTextView.setText(mTitle);
                //撤销申请
                TextView mCancelTextView = holder.getView(R.id.id_layout_1_cancel);
                //修改申请
                TextView mUpdataTextView = holder.getView(R.id.id_layout_1_updata);
                if ("1".equals(itemBean.getState())) {
                    setEnabledTrue(mUpdataTextView, true);
                    mUpdataTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dataBean == null){
                                mActivity.startActivity(ApplySalesActivity.newIntent(mContext, itemBean,itemBean.getPrice(),itemBean.getNum()));
                            }else {
                                mActivity.startActivity(ApplySalesActivity.newIntent(mContext, itemBean,dataBean.getPrice(),dataBean.getNum()));
                            }

                        }
                    });
                    setEnabledTrue(mCancelTextView, false);
                    mCancelTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel(id);
                        }
                    });
                } else {
                    setEnabledFalse(mUpdataTextView);
                    setEnabledFalse(mCancelTextView);
                }

                //倒计时
                TextView mTipTextView1 = holder.getView(R.id.id_layout_1_tip);
                setTimer(holder, itemBean, mTipTextView1);
                //图片
                LinearLayout mImagLayout = holder.getView(R.id.id_layout_1_images);
                RecyclerView mRecyclerView = holder.getView(R.id.id_rli_list);
                if (itemBean.getImg() != null && itemBean.getImg().size() > 0) {
                    mImagLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
                    // 禁止RecyclerView滑动解决RecyclerView不显示问题
                    mRecyclerView.setNestedScrollingEnabled(false);
                    AfterSaleImageAdapter mAfterSaleImageAdapter = new AfterSaleImageAdapter(mContext, R.layout.adapter_as_image);
                    mAfterSaleImageAdapter.addList(itemBean.getImg());
                    mRecyclerView.setAdapter(mAfterSaleImageAdapter);
                } else {
                    mImagLayout.setVisibility(View.GONE);
                }
                break;
            case 2:
                mLayout2.setVisibility(View.VISIBLE);
                LinearLayout mLinearLayout2 = holder.getView(R.id.id_layout_2_content);
                setContentBg(mLinearLayout2, classType);
                //地址
                TextView mAddressTextView = holder.getView(R.id.id_layout_2_address);
                String mStoreAddress = itemBean.getStore_address();
                mAddressTextView.setText(Spans.builder().text("退货地址：", 14, 0xff999999).text(mStoreAddress).build());
                //收件人
                TextView mPepleTextView = holder.getView(R.id.id_layout_2_peple);
                mPepleTextView.setText(itemBean.getStore_username() + "    " + itemBean.getStore_phone());
                //时间
                TextView mTimeTextView2 = holder.getTextView(R.id.id_layout_2_time);
                mTimeTextView2.setText(DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getStatetime()) * 1000));
                //状态
                TextView mStateTextView2 = holder.getTextView(R.id.id_layout_2_state);
                mStateTextView2.setText(itemBean.getTitle());
                //撤销申请
                TextView mCancelTextView2 = holder.getView(R.id.id_layout_2_cancel);
                //退货方式
                TextView mUpdataTextView2 = holder.getView(R.id.id_layout_2_updata);
                if ("4".equals(itemBean.getState())) {
                    setEnabledTrue(mCancelTextView2, false);
                    setEnabledTrue(mUpdataTextView2, true);
                    mCancelTextView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel(id);
                        }
                    });
                    mUpdataTextView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.startActivity(SalesReturnWayActivity.newIntent(mContext, itemBean.getStore_title(), id));
                        }
                    });
                } else {
                    setEnabledFalse(mCancelTextView2);
                    setEnabledFalse(mUpdataTextView2);
                }
                //倒计时
                TextView mTipTextView2 = holder.getView(R.id.id_layout_2_tip);
                setTimer(holder, itemBean, mTipTextView2);
                break;
            case 3:
                mLayout3.setVisibility(View.VISIBLE);
                LinearLayout mLinearLayout3 = holder.getView(R.id.id_layout_3_content);
                setContentBg(mLinearLayout3, classType);
                //状态
                TextView mTitleTextView3 = holder.getView(R.id.id_layout_3_state);
                mTitleTextView3.setText(itemBean.getTitle());
                //退货方式
                TextView mTuiModeTextView3 = holder.getView(R.id.id_layout_3_type);
                //上门时间/退货门店
                TextView mTimeStoreTextView3 = holder.getView(R.id.id_layout_3_time_store);
                String mTuiModeStr3 = itemBean.getQu_mode();
                if ("1".equals(mTuiModeStr3)) {
                    mTuiModeTextView3.setText(Spans.builder().text("退货方式：", 14, 0xff999999).text("送至门店").build());
                    mTimeStoreTextView3.setText(Spans.builder().text("退货门店：", 14, 0xff999999).text(itemBean.getStore_title()).build());
                }
                //备注
                TextView mNoteTextView3 = holder.getView(R.id.id_layout_3_note);
                TextViewUtils.setText(mNoteTextView3, itemBean.getDoot_content());
                //时间
                TextView mTimeTextView3 = holder.getTextView(R.id.id_layout_3_time);
                mTimeTextView3.setText(DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getUser_queren_time()) * 1000));
                //倒计时
                TextView mTipTextView3 = holder.getView(R.id.id_layout_3_tip);
                setTimer(holder, itemBean, mTipTextView3);
                break;
            case 4:
                mLayout3.setVisibility(View.VISIBLE);
                LinearLayout mLinearLayout4 = holder.getView(R.id.id_layout_3_content);
                setContentBg(mLinearLayout4, classType);
                //状态
                TextView mTitleTextView4 = holder.getView(R.id.id_layout_3_state);
                mTitleTextView4.setText(itemBean.getTitle());
                //退货方式
                TextView mTuiModeTextView4 = holder.getView(R.id.id_layout_3_type);
                //上门时间/退货门店
                TextView mTimeStoreTextView4 = holder.getView(R.id.id_layout_3_time_store);
                String mTuiModeStr4 = itemBean.getQu_mode();
                if ("2".equals(mTuiModeStr4)) {
                    mTuiModeTextView4.setText(Spans.builder().text("退货方式：", 14, 0xff999999).text("上门取件").build());
                    mTimeStoreTextView4.setText(Spans.builder().text("上门时间：", 14, 0xff999999)
                            .text(DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getDoor_time()) * 1000)).build());
                }
                //备注
                TextView mNoteTextView4 = holder.getView(R.id.id_layout_3_note);
                TextViewUtils.setText(mNoteTextView4, itemBean.getDoot_content());
                //时间
                TextView mTimeTextView4 = holder.getTextView(R.id.id_layout_3_time);
                mTimeTextView4.setText(DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getUser_queren_time()) * 1000));
                //倒计时
                TextView mTipTextView4 = holder.getView(R.id.id_layout_3_tip);
                setTimer(holder, itemBean, mTipTextView4);
                break;
            case 5:
                mLayout5.setVisibility(View.VISIBLE);
                LinearLayout mLinearLayout5 = holder.getView(R.id.id_layout_5_contents);
                setContentBg(mLinearLayout5, classType);
                //状态
                TextView mStateTextView5 = holder.getView(R.id.id_layout_5_state);
                mStateTextView5.setText(itemBean.getTitle());
                //内容
                TextView mContentTextView5 = holder.getTextView(R.id.id_layout_5_content);
                mContentTextView5.setText(itemBean.getContent());
                //时间
                TextView mTimeTextView5 = holder.getTextView(R.id.id_layout_5_time);
                mTimeTextView5.setText(DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getTime()) * 1000));
                break;
        }
    }

    /**
     * 撤销申请
     */
    private void cancel(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(URLConstants.REQUEST_CANCEL_REFUND_GOODS, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) mContext) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        ToastManager.showShortToast(mContext, "撤销成功");
                        EventBus.getDefault().postSticky(new AfterSaleDetailEvent(true));
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        ToastManager.showShortToast(mContext, "撤销失败");
                    }
                });
    }

    /**
     * 设置倒计时
     *
     * @param holder
     * @param itemBean
     * @param textView
     */
    private void setTimer(BaseViewHolder holder, RefundDetail.RefundDataBean itemBean, TextView textView) {
        // 判断1、4状态，有倒计时，后面的字段拼接
        if ("1".equals(itemBean.getCountdown())) {// 1 倒计时
            String nowTime = (System.currentTimeMillis() / 1000) + "";
            OrderUtils.setRefundCountDownTimer(textView, holder, nowTime, itemBean.getEndtime(), itemBean.getState_title());
        } else {// 2 不倒计时
            textView.setVisibility(View.VISIBLE);
            textView.setText(itemBean.getState_title());
        }
    }

    /**
     * 设置不可点击
     *
     * @param textView
     */
    private void setEnabledFalse(TextView textView) {
        textView.setEnabled(false);
        textView.setTextColor(0xffffffff);
        textView.setBackgroundResource(R.drawable.shape_gray_enable_false);
    }

    /**
     * 设置可点击
     *
     * @param textView
     */
    private void setEnabledTrue(TextView textView, boolean isLeft) {
        textView.setEnabled(true);
        textView.setTextColor(0xffff725c);
        textView.setBackgroundResource(R.drawable.shape_orange_stroke);
    }

    /**
     * 设置内容背景色
     *
     * @param layout
     * @param classType
     */
    private void setContentBg(LinearLayout layout, String classType) {
        if ("1".equals(classType)) {//右
            layout.setBackgroundResource(R.drawable.shape_grayf_radius_fdf5ff);
        } else {//左
            layout.setBackgroundResource(R.drawable.shape_grayf_radius_f1f4ff);
        }
    }
}
