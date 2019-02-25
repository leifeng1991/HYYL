package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.event.ShopThemeAddShopCartEvent;
import com.xxzlkj.shop.model.ShopActionThemeBean;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;


public class ShopActionThemItemAdapter extends BaseAdapter<ShopActionThemeBean.DataBean.BrandBean.GoodsBean> {
    public ShopActionThemItemAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopActionThemeBean.DataBean.BrandBean.GoodsBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_sati_image);// 商品图片
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图片图层
        ImageView mAddShopCartImageView = holder.getView(R.id.id_sati_add_shopcart);// 加入购车按钮
        LinearLayout mYuShouLayout = holder.getView(R.id.id_yushou_layout);// 预售字样布局
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 预售字样
        TextView mTitleTextView = holder.getView(R.id.id_sati_title);// 标题
        TextView mAdsTextView = holder.getView(R.id.id_sati_ads);// 广告语
        TextView mPriceTextView = holder.getView(R.id.id_sati_price);// 现价
        TextView mPricesTextView = holder.getView(R.id.id_sati_prices);// 原价
        // 设置商品图片
        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext, mImageView, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
        // 设置商品标题
        mTitleTextView.setText(itemBean.getTitle());
        // 设置商品ads
        mAdsTextView.setText(itemBean.getAds());
        // 设置商品价格
        String price = itemBean.getPrice();
        mPriceTextView.setText("￥" + price);
        String prices = itemBean.getPrices();
        TextPriceUtil.setTextPrices(price, prices, mPricesTextView);
        // 加入购物车
        mAddShopCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShopThemeAddShopCartEvent(itemBean.getId(), v));
            }
        });

        // 控制预售字样的显示和隐藏
        GoodsUtils.setYuShouVisible(itemBean.getActivitys(), mAddShopCartImageView, mYuShouButton, mYuShouLayout);
    }
}
