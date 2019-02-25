package com.xxzlkj.shop.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * by Administrator on 2017/11/8.
 */

public class GoodsUtils {

    /**
     * @param activitys  活动 1:预售 2：团购(必传)
     * @param view       购物车/去抢购控件 可以为空
     * @param button     预售/团购控件 (必传)
     * @param layoutView 预售/团购布局  可以为空,隐藏和显示时布局时必传
     */
    public static void setYuShouVisible(String activitys, View view, CustomButton button, View layoutView) {
        if ("1".equals(activitys) || "2".equals(activitys)) {
            // 预售、团购
            // 隐藏购物车/去抢购控件 显示预售/团购字样
            if (view != null)
                view.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            if (layoutView != null)
                button.setVisibility(View.VISIBLE);
            button.setText("1".equals(activitys) ? "预售" : "团购");
        } else {
            // 显示购物车/去抢购控件 隐藏预售字样
            if (view != null)
                view.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            if (layoutView != null)
                button.setVisibility(View.GONE);

        }
    }

    /**
     * @param bottomImageView 商品图片
     * @param topImageView    商品图层图片
     * @param simg            商品图片地址
     * @param activityIconImg 商品图层图片地址
     */
    public static void setGoodsCoverageImage(Context context, ImageView bottomImageView, ImageView topImageView, String simg, String activityIconImg) {
        PicassoUtils.setImageBig(context, simg, bottomImageView);
        // 图层地址为空不显示该图层
        if (TextUtils.isEmpty(activityIconImg)){
            topImageView.setVisibility(View.GONE);
        }else {
            topImageView.setVisibility(View.VISIBLE);
            PicassoUtils.setImageBig(context, activityIconImg, topImageView);
        }

    }
}
