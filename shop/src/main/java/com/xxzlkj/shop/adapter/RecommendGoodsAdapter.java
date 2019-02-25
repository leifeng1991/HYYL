package com.xxzlkj.shop.adapter;


import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 为你推荐
 * Created by Administrator on 2017/3/23.
 */

public class RecommendGoodsAdapter extends BaseAdapter<Goods> {
    public RecommendGoodsAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        ImageView mGirdGoodsImage = holder.getView(R.id.id_hii_image);// 商品图片
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图图层
        TextView mGoodsTitle = holder.getView(R.id.id_hii_text);// 标题
        TextView mGoodsGuige = holder.getView(R.id.id_hii_norms);// 规格
        LinearLayout mPriceLayout = holder.getView(R.id.id_price_layout);// 价格布局
        TextView mGoodsPrice = holder.getView(R.id.id_hii_price);// 现价
        TextView mGoodsPrices = holder.getView(R.id.id_hii_price_line);// 原价
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 预售

        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext, mGirdGoodsImage, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
        TextViewUtils.setText(mGoodsTitle,itemBean.getTitle());
        TextViewUtils.setText(mGoodsGuige,itemBean.getAds());
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextPriceUtil.setTextPrices(price,prices,mGoodsPrices);
        TextViewUtils.setText(mGoodsPrice,"￥" + price);

        if ("1".equals(itemBean.getActivitys())){
            // 预售
            mPriceLayout.setOrientation(LinearLayout.VERTICAL);
        }else {
            // 其他
            mPriceLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        // 控制预售字样的显示和隐藏
        GoodsUtils.setYuShouVisible(itemBean.getActivitys(), null, mYuShouButton, null);
    }
}
