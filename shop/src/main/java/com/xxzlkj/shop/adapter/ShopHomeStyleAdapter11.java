package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.event.AddShopCartActionEvent;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ShopHomeStyleAdapter11 extends BaseAdapter<Goods> {
    private Activity mActivity;
    // ture一级界面 false:二级界面
    private boolean flag;

    public ShopHomeStyleAdapter11(Context context, Activity mActivity, boolean flag, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.flag = flag;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        ImageView mImageView = holder.getView(R.id.id_shs11_image);// 商品图片
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图片图层
        ImageView mAddShopCartImageView = holder.getView(R.id.id_shs11_add_shopcart);// 加入购物车
        LinearLayout mYuShouLayout = holder.getView(R.id.id_yushou_layout);// 预售字样布局
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 预售字样
        TextView mTitleTextView = holder.getView(R.id.id_shs11_title);// 标题
        TextView mPriceTextView = holder.getView(R.id.id_shs11_price);// 现价
        TextView mPricesTextView = holder.getView(R.id.id_shs11_prices);// 原价
        View rightLine = holder.getView(R.id.id_shs11_right_line);
        // 右边线的显示和隐藏
        if ((position + 1) % 3 == 0) {
            rightLine.setVisibility(View.GONE);
        } else {
            rightLine.setVisibility(View.VISIBLE);
        }
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextPriceUtil.setTextPrices(price, prices, mPricesTextView);
        mTitleTextView.setText(itemBean.getTitle());
        mPriceTextView.setText("￥" + price);


        // 添加购物车
        mAddShopCartImageView.setOnClickListener(v -> {
            if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                EventBus.getDefault().postSticky(new AddShopCartActionEvent(v, itemBean, flag));
            }
        });

        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext,mImageView,mCoverageImageView,itemBean.getSimg(),itemBean.getActivity_icon_img());
        // 控制预售字样的显示和隐藏
        GoodsUtils.setYuShouVisible(itemBean.getActivitys(), mAddShopCartImageView, mYuShouButton, mYuShouLayout);
    }
}
