package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.model.TuanGou;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 团购
 * Created by Administrator on 2017/3/24.
 */

public class TuanShopAdapter extends BaseAdapter<TuanGou> {
    private Activity mActivity;

    public TuanShopAdapter(Context context, Activity mActivity,int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final TuanGou itemBean) {
        ImageView mTuanImageView = holder.getView(R.id.id_tuan_image);
        TextView mTuanTitle = holder.getView(R.id.id_tuan_title);
        TextView mTuanGuiGe = holder.getView(R.id.id_tuan_guige);
        TextView mTuanBuyNow = holder.getView(R.id.id_tuan_buy_now);
        TextView mTuanPrice = holder.getView(R.id.id_tuan_price);
        TextView mTuanPrices = holder.getView(R.id.id_tuan_prices);
        TextView mTuanPeples = holder.getView(R.id.id_tuan_peples);
        View mLine = holder.getView(R.id.id_tuan_line);

        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mTuanImageView);
        TextViewUtils.setText(mTuanTitle,itemBean.getTitle());
        TextViewUtils.setText(mTuanGuiGe,itemBean.getAds());
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextViewUtils.setText(mTuanPrice,"￥" + price);
        TextPriceUtil.setTextPrices(price,prices,mTuanPrices);

        if (position == getItemCount() - 1){
            mLine.setVisibility(View.GONE);
        }else {
            mLine.setVisibility(View.VISIBLE);
        }

        mTuanBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null){
                    mActivity.startActivity(GoodsDetailActivity.newIntent(mContext,itemBean.getGoods_id()));
                }
            }
        });
    }
}
