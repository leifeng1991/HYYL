package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 商城热门推荐下部适配器
 * Created by Administrator on 2017/3/13.
 */

public class HotRecommentAdapter extends BaseAdapter<Goods> {
    private Activity mActivity;

    public HotRecommentAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Goods itemBean) {
        ImageView mImageView = holder.getView(R.id.id_hii_image);// 商品图片
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图片图层
        TextView mDetailTextView = holder.getView(R.id.id_hii_text);// 标题
        TextView mPriceTextView = holder.getView(R.id.id_hii_price);// 现价
        TextView mNormsTextView = holder.getView(R.id.id_hii_norms);// 规格
        TextView mLineTextView = holder.getView(R.id.id_hii_price_line);// 原价
        TextView mBuyNowTextView = holder.getView(R.id.id_hii_bn);// 立即购买
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 预售

        mBuyNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到商品详情页面
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getId()));
            }
        });

        String prices = itemBean.getPrices();
        String price = itemBean.getPrice();
        TextPriceUtil.setTextPrices(price, prices, mLineTextView);
        TextViewUtils.setText(mNormsTextView, itemBean.getAds());

        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext, mImageView, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
        TextViewUtils.setText(mDetailTextView, itemBean.getTitle());
        TextViewUtils.setText(mPriceTextView, "￥" + price);

        // 控制预售字样的显示和隐藏
        GoodsUtils.setYuShouVisible(itemBean.getActivitys(), mBuyNowTextView, mYuShouButton, null);
    }

}
