package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shopOrder.AfterSalesDetailActivity;
import com.xxzlkj.shop.model.RefundGoods;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 退款详情列表适配器
 * Created by Administrator on 2017/4/10.
 */

public class RefundDetailAdapter extends BaseAdapter<RefundGoods.DataBean> {
    private Activity mActivity;

    public RefundDetailAdapter(Context context,Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final RefundGoods.DataBean itemBean) {
        ImageView mThemeImageView = holder.getView(R.id.id_ali_zhaolin_image);
        ImageView mGoodsImageView = holder.getView(R.id.id_ali_image);
        TextView mThemeTextView = holder.getView(R.id.id_ali_theme);
        TextView mStateTextView = holder.getView(R.id.id_ali_state);
        TextView mTitleTextView = holder.getView(R.id.id_ali_title);
        TextView mAdsTextView = holder.getView(R.id.id_ali_ads);
        TextView mPriceTextView = holder.getView(R.id.id_ali_price);
        TextView mPricesTextView = holder.getView(R.id.id_ali_prices);
        TextView mNumberTextView = holder.getView(R.id.id_ali_number);
        TextView mTimeTextView = holder.getView(R.id.id_ali_time);
        TextView mTotalPriceTextView = holder.getView(R.id.id_ali_total_price);
        TextView mDetailTextView = holder.getView(R.id.id_ali_detail);

        mDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(AfterSalesDetailActivity.newIntent(mContext,itemBean));
            }
        });

        String strState = null;
        switch (NumberFormatUtils.toInt(itemBean.getState())){
            case 1:
                strState = "退款中";
                break;
            case 2:
                strState = "退款完成";
                break;
            case 3:
                strState = "退款取消";
                break;
            case 4:
                strState = "退款中";
                break;
        }

        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mGoodsImageView);
        TextViewUtils.setText(mStateTextView,strState);
        TextViewUtils.setText(mTitleTextView,itemBean.getTitle());
        TextViewUtils.setText(mAdsTextView,itemBean.getAds());
        TextViewUtils.setText(mPriceTextView,"￥" + itemBean.getPrice());
        TextViewUtils.setText(mNumberTextView,"X" + itemBean.getNum());
        TextViewUtils.setText(mTimeTextView, DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000));
        TextViewUtils.setText(mTotalPriceTextView, itemBean.getRefund_price());
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mPricesTextView);// 设置原价

    }
}
