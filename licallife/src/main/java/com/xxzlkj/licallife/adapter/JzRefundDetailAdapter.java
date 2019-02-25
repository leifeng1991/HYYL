package com.xxzlkj.licallife.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.LocalLifeRefundGoodsInfoActivity;
import com.xxzlkj.licallife.model.LocaLifeRefundGoods;
import com.xxzlkj.licallife.utils.LocalLifeUnitUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 退款详情列表适配器(本地生活)
 * Created by Administrator on 2017/4/10.
 */

public class JzRefundDetailAdapter extends BaseAdapter<LocaLifeRefundGoods.DataBean> {
    private final Activity mActivity;

    public JzRefundDetailAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final LocaLifeRefundGoods.DataBean itemBean) {
        ImageView mThemeImageView = holder.getView(R.id.id_ali_zhaolin_image);
        ImageView mGoodsImageView = holder.getView(R.id.id_ali_image);
        TextView mThemeTextView = holder.getView(R.id.id_ali_theme);
        TextView mStateTextView = holder.getView(R.id.id_ali_state);
        TextView mTitleTextView = holder.getView(R.id.id_ali_title);
        TextView mServiceTimeTextView = holder.getView(R.id.id_ali_service_time);
        TextView mServiceHoursTextView = holder.getView(R.id.id_ali_service_hours);
        TextView mPriceTextView = holder.getView(R.id.id_ali_price);
        TextView mPricesTextView = holder.getView(R.id.id_ali_prices);
        TextView mNumberTextView = holder.getView(R.id.id_ali_number);
        TextView mTimeTextView = holder.getView(R.id.id_ali_time);
        TextView mTotalPriceTextView = holder.getView(R.id.id_ali_total_price);
        TextView mDetailTextView = holder.getView(R.id.id_ali_detail);
        // 跳转到详情
        mDetailTextView.setOnClickListener(v -> mContext.startActivity(LocalLifeRefundGoodsInfoActivity.newIntent(mContext, itemBean.getId())));

        String strState = null;
        switch (NumberFormatUtils.toInt(itemBean.getState())) {
            case 1:
                strState = "退款中";
                break;
            case 2:
                strState = "退款完成";
                break;
            case 3:
                strState = "退款取消";
                break;
            case 5:
                strState = "退款取消";
                break;
        }

        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mGoodsImageView);
        TextViewUtils.setText(mStateTextView, strState);
        TextViewUtils.setText(mTitleTextView, itemBean.getTitle());
        mServiceTimeTextView.setText(LocalLifeUnitUtils.getUnitContent(itemBean.getNum(), itemBean.getUnit(), itemBean.getUnit_desc()));//服务时长：数量
        TextViewUtils.setText(mServiceHoursTextView, "服务时间：" + DateUtils.getYearMonthDay(NumberFormatUtils.toLong(itemBean.getEndtime()) * 1000, "yyyy.MM.dd HH:mm"));
        TextViewUtils.setText(mPriceTextView, "￥" + itemBean.getPrice());
        TextViewUtils.setText(mNumberTextView, "X" + itemBean.getNum());
        TextViewUtils.setText(mTimeTextView, DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000));
        TextViewUtils.setText(mTotalPriceTextView, "￥" + itemBean.getRefund_price());
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mPricesTextView);// 设置原价


    }
}
