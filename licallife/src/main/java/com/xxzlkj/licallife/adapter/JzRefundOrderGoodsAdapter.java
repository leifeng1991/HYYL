package com.xxzlkj.licallife.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.LocalLifeAddRefundActivity;
import com.xxzlkj.licallife.model.JzRefundOrderGoods;
import com.xxzlkj.licallife.utils.LocalLifeUnitUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 可申请售后商品订单列表适配器(本地生活)
 * Created by Administrator on 2017/4/7.
 */

public class JzRefundOrderGoodsAdapter extends BaseAdapter<JzRefundOrderGoods.DataBean> {

    private final Activity mActivity;

    public JzRefundOrderGoodsAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final JzRefundOrderGoods.DataBean itemBean) {
        JzRefundOrderGoods.DataBean.GoodsBean goodsBean = itemBean.getGoods().get(0);
        ImageView mImageView = holder.getView(R.id.id_jali_image);
        TextView mTitleTextView = holder.getView(R.id.id_jali_title);
        TextView mStateTextView = holder.getView(R.id.id_jali_state);
        TextView mTimeTextView = holder.getView(R.id.id_jali_time);
        TextView mHoursTextView = holder.getView(R.id.id_jali_hours);
        TextView mPriceTextView = holder.getView(R.id.id_jali_price);
        TextView mApplyTextView = holder.getView(R.id.id_jali_apply);

        PicassoUtils.setImageSmall(mContext, goodsBean.getSimg(), mImageView);
        TextViewUtils.setText(mTitleTextView, goodsBean.getTitle());
        TextViewUtils.setText(mPriceTextView, "￥" + itemBean.getPrice());
        mHoursTextView.setText(LocalLifeUnitUtils.getUnitContent(goodsBean.getNum(), goodsBean.getUnit(), goodsBean.getUnit_desc()));//服务时长：数量

        TextViewUtils.setText(mTimeTextView, "服务时间：" +
                DateUtils.getYearMonthDay(NumberFormatUtils.toLong(itemBean.getEndtime()) * 1000, "yyyy.MM.dd HH:mm"));

        mApplyTextView.setOnClickListener(v -> mActivity.startActivity(LocalLifeAddRefundActivity.newIntent(
                mContext, itemBean.getId(), itemBean.getPrice(),
                itemBean.getGoods().get(0).getId(), false)));

    }
}
