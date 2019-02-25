package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shopOrder.ApplySalesActivity;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 可以申请售后的商品
 * Created by Administrator on 2017/4/8.
 */

public class RefundOrderGoodsItemAdapter extends BaseAdapter<Goods> {
    // 售后订单id
    private String mOrderId;
    // 折扣系数
    private String discount;
    private Activity mActivity;

    public RefundOrderGoodsItemAdapter(Context context, Activity mActivity,int itemId,String mOrderId,String discount) {
        super(context, itemId);
        this.mOrderId = mOrderId;
        this.discount = discount;
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Goods itemBean) {
        ImageView mImageView = holder.getView(R.id.id_ali_image);
        TextView mTitleTextView = holder.getView(R.id.id_ali_title);
        TextView mAdsTextView = holder.getView(R.id.id_ali_ads);
        TextView mPriceTextView = holder.getView(R.id.id_ali_price);
        TextView mPricesTextView = holder.getView(R.id.id_ali_prices);
        TextView mNumberTextView = holder.getView(R.id.id_ali_number);
        TextView mApplySalesTextView = holder.getView(R.id.id_ali_after_sales);
        mApplySalesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到申请售后填写界面
                mActivity.startActivity(ApplySalesActivity.newIntent(
                        mContext,mOrderId,itemBean.getId(),
                        itemBean.getPrice(),itemBean.getNum(),discount,itemBean.getCoupon_price()));
            }
        });

        TextViewUtils.setText(mTitleTextView,itemBean.getTitle());
        TextViewUtils.setText(mAdsTextView,itemBean.getAds());
        TextViewUtils.setText(mPriceTextView,"￥" + itemBean.getPrice());
        TextViewUtils.setText(mNumberTextView,"X " + itemBean.getNum());
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mPricesTextView);// 设置原价

        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mImageView);
    }
}
