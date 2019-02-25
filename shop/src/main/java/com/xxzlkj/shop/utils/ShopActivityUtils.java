package com.xxzlkj.shop.utils;

import android.app.Activity;

import com.xxzlkj.shop.activity.ShopHomeActivity;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/26 11:49
 */
public class ShopActivityUtils {
    /**
     * 跳到商城首页
     *
     * @param type 2 火爆预售;3 兆邻团购 ;4 果蔬生鲜
     */
    public static void jumpToShopHomeActivity(Activity activity, String... type) {
        activity.startActivity(ShopHomeActivity.newIntent(activity.getApplicationContext(), type != null && type.length > 0 ? type[0] : null));
    }

}
