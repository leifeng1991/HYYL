package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


public class TuanGouStyleAdapter3 extends BaseAdapter<Goods> {
    private Activity mActivity;

    public TuanGouStyleAdapter3(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Goods itemBean) {
        ImageView mTuanImageView = holder.getView(R.id.id_tuan_image);// 商品图片
        TextView mTuanTitle = holder.getView(R.id.id_tuan_title);// 标题
        TextView mTuanGuiGe = holder.getView(R.id.id_tuan_guige);// 规格
        TextView mTuanBuyNow = holder.getView(R.id.id_tuan_buy_now);// 立即购买
        TextView mTuanPrice = holder.getView(R.id.id_tuan_price);// 现价
        TextView mTuanPrices = holder.getView(R.id.id_tuan_prices);// 原价
        TextView mTuanPeples = holder.getView(R.id.id_tuan_peples);// 几人团
        View mLine = holder.getView(R.id.id_tuan_line);

        // 设置数据
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mTuanImageView);
        TextViewUtils.setText(mTuanTitle, itemBean.getTitle());
        TextViewUtils.setText(mTuanGuiGe, itemBean.getAds());
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextViewUtils.setText(mTuanPrice, "￥" + price);
        TextPriceUtil.setTextPrices(price, prices, mTuanPrices);

        mTuanPeples.setText(String.format("%s人团", itemBean.getNum()));
        // 控制分割线的显示和隐藏
        mLine.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);

        mTuanBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                    mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getGoods_id()));
                }
            }
        });
    }
}
